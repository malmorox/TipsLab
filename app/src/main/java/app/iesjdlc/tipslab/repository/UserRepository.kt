package app.iesjdlc.tipslab.repository

import android.util.Log
import app.iesjdlc.tipslab.mappers.UserMapper
import app.iesjdlc.tipslab.models.domain.User
import app.iesjdlc.tipslab.models.dto.UserDto
import app.iesjdlc.tipslab.utils.FirebaseClient
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseClient.db
    private val mapper = UserMapper()

    suspend fun existsUser(uid: String): Result<Boolean> {
        return try {
            val doc = db.collection("users").document(uid).get().await()
            Result.success(doc.exists())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun existsEmail(email: String): Result<Boolean> {
        return try {
            val snapshot = db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()
            Result.success(!snapshot.isEmpty)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun existsUsername(username: String): Result<Boolean> {
        return try {
            val snapshot = db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .await()
            Result.success(!snapshot.isEmpty)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEmailByUsername(username: String): Result<String?> {
        return try {
            val snapshot = db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .await()
            Result.success(snapshot.documents.firstOrNull()?.getString("email"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getUserById(id: String): Result<User> {
        return try {
            val doc = db.collection("users").document(id).get().await()
            doc.toObject(UserDto::class.java)?.let { Result.success(mapper.toDomain(it)) }
                ?: Result.failure(Exception("Usuario no encontrado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Función para crear un nuevo usuario en Firestore
    suspend fun createUser(user: User): Result<Unit> {
        return try {
            val dto = mapper.toDto(user)
            db.collection("users").document(user.id).set(dto).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Crea el documento de un nuevo usuario de Google en Firestore
    suspend fun createGoogleUser(user: User): Result<Unit> {
        return try {
            val dto = mapper.toDto(user)
            db.collection("users").document(dto.id).set(dto).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(user: User): Result<Unit> {
        return try {
            val dto = mapper.toDto(user)
            db.collection("users").document(user.id).set(dto).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}