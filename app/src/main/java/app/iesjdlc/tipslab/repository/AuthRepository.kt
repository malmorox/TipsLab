package app.iesjdlc.tipslab.repository

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.iesjdlc.tipslab.mappers.UserMapper
import app.iesjdlc.tipslab.models.domain.User
import app.iesjdlc.tipslab.models.dto.UserDto
import app.iesjdlc.tipslab.utils.FirebaseClient
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseClient.auth
    private val db = FirebaseClient.db

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

    suspend fun login(
        emailOrUsername: String,
        password: String
    ): Result<String> {
        return try {
            val email = if (emailOrUsername.contains("@")) {
                emailOrUsername
            } else {
                getEmailByUsername(emailOrUsername) ?: return Result.failure(Exception("Nombre de usuario no encontrado"))
            }

            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Usuario nulo"))

            fetchUserData(firebaseUser.uid)
            Result.success(firebaseUser.uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(
        email: String,
        username: String,
        password: String
    ): Result<String> {
        return try {
            if (existsUsername(username))
                return Result.failure(Exception("El nombre de usuario ya está en uso"))

            if (existsEmail(email))
                return Result.failure(Exception("El email ya está en uso"))

            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Error al crear usuario"))

            val dto = UserDto(
                id = firebaseUser.uid,
                email = email,
                username = username
            )
            db.collection("users").document(firebaseUser.uid).set(dto).await()

            userProfile = mapper.toDomain(dto)
            Result.success(firebaseUser.uid)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogle(idToken: String): Result<String> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Usuario nulo"))

            val userDoc = db.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()

            if (!userDoc.exists()) {
                // Si es la primera vez con Google, creamos el documento con sus datos
                val dto = UserDto(
                    id = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    username = firebaseUser.displayName ?: "",
                    photo_url = firebaseUser.photoUrl?.toString() ?: "",
                    provider = "GOOGLE"
                )
                db.collection("users")
                    .document(firebaseUser.uid)
                    .set(dto)
                    .await()

                userProfile = mapper.toDomain(dto)
            } else {
                // Si ya existe cargamos el usuario
                fetchUserData(firebaseUser.uid)
            }

            Result.success(firebaseUser.uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun isLoggedIn(): Boolean = auth.currentUser != null

    // Funciones de utilidad para verificar si un email o username ya están en uso
    suspend fun existsEmail(email: String): Boolean {
        val snapshot = db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()
        return !snapshot.isEmpty
    }

    suspend fun existsUsername(username: String): Boolean {
        val snapshot = db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .await()
        return !snapshot.isEmpty
    }

    suspend fun getEmailByUsername(username: String): String? {
        val snapshot = db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .await()
        return snapshot.documents.firstOrNull()?.getString("email")
    }
}