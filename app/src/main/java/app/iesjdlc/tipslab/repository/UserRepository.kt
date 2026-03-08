package app.iesjdlc.tipslab.repository

import app.iesjdlc.tipslab.mappers.UserMapper
import app.iesjdlc.tipslab.models.domain.User
import app.iesjdlc.tipslab.models.dto.UserDto
import app.iesjdlc.tipslab.utils.FirebaseClient
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseClient.db
    private val mapper = UserMapper()

    suspend fun getUserById(id: String): User {
        val doc = db.collection("users").document(id).get().await()
        return mapper.toDomain(
            doc.toObject(UserDto::class.java) ?: throw Exception("Usuario no encontrado")
        )
    }
}