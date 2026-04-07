package app.iesjdlc.tipslab.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.iesjdlc.tipslab.mappers.UserMapper
import app.iesjdlc.tipslab.models.domain.User
import app.iesjdlc.tipslab.models.dto.UserDto
import app.iesjdlc.tipslab.utils.FirebaseClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseClient.auth,
    private val db: FirebaseFirestore = FirebaseClient.db
) {
    private val mapper = UserMapper()

    // Para guardar los datos del usuario en memoria
    var userProfile by mutableStateOf<User?>(null)
        private set

    // Verificamos si hay ya un usuario al iniciar la aplicación
    suspend fun checkUserSession(): Boolean {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            // Si hay sesión, cargamos sus datos de Firestore
            fetchUserData(firebaseUser.uid)
            return true
        }
        return false
    }

    // Cargamos los datos de Firestore y los convertimos a nuestro modelo de dominio (el que usamos en el UI)
    private suspend fun fetchUserData(uid: String) {
        try {
            val snapshot = db.collection("users").document(uid).get().await()
            val dto = snapshot.toObject(UserDto::class.java)
            userProfile = mapper.toDomain(dto ?: throw Exception("Usuario no encontrado") )
        } catch (_: Exception) {
            userProfile = null
        }
    }

    // Función para cargar el perfil del usuario desde UseCases
    suspend fun loadProfile(uid: String) {
        fetchUserData(uid)
    }

    suspend fun login(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Usuario nulo"))
            Result.success(firebaseUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Error al crear usuario"))
            Result.success(firebaseUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Solo autentica con Firebase y comprueba si el usuario existe en Firestore.
     * Si ya existe, carga su perfil. Si es nuevo, devuelve isNewUser = true
     * sin crear ningún documento (eso lo hace el UseCase).
     */
    suspend fun authenticateWithGoogle(idToken: String): Result<GoogleAuthResult> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Usuario nulo"))

            val userDoc = db.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()

            val isNewUser = !userDoc.exists()

            if (!isNewUser) {
                fetchUserData(firebaseUser.uid)
                return Result.success(GoogleAuthResult.ExistingUser)
            }

            Result.success(
                GoogleAuthResult.NewUser(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName,
                    photoUrl = firebaseUser.photoUrl?.toString()
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
        userProfile = null
    }
}

sealed class GoogleAuthResult {
    // El usuario ya tenía cuenta y su perfil ya está cargado en memoria
    data object ExistingUser : GoogleAuthResult()

    // Primera vez con Google y hay que crear el documento en Firestore
    data class NewUser(
        val uid: String,
        val email: String,
        val displayName: String?,
        val photoUrl: String?
    ) : GoogleAuthResult()
}
