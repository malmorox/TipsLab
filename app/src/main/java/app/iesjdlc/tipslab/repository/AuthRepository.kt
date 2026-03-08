package app.iesjdlc.tipslab.repository

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.iesjdlc.tipslab.mappers.UserMapper
import app.iesjdlc.tipslab.models.domain.User
import app.iesjdlc.tipslab.models.dto.UserDto
import app.iesjdlc.tipslab.utils.FirebaseClient
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseClient.auth
    private val db = FirebaseClient.db

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
            userProfile = UserMapper().toDomain(dto ?: throw Exception("Usuario no encontrado") )
        } catch (_: Exception) {
            userProfile = null
        }
    }

    suspend fun login(
        emailOrUsername: String,
        password: String
    ): Result<String> {
        return Result.success("")
    }

    suspend fun signUp(
        email: String,
        username: String,
        password: String
    ): Result<String> {
        return Result.success("")
    }

    fun logout() {
        auth.signOut()
    }

    fun isLoggedIn(): Boolean = auth.currentUser != null
}