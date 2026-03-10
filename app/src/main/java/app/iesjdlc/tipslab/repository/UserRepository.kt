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

    suspend fun getUserById(id: String): Result<User> {
        return try {
            val doc = db.collection("users").document(id).get().await()
            doc.toObject(UserDto::class.java)?.let { Result.success(mapper.toDomain(it)) }
                ?: Result.failure(Exception("Usuario no encontrado"))
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