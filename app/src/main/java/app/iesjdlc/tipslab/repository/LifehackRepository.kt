package app.iesjdlc.tipslab.repository

import app.iesjdlc.tipslab.mappers.LifehackMapper
import app.iesjdlc.tipslab.models.domain.Lifehack
import app.iesjdlc.tipslab.models.dto.LifehackDto
import app.iesjdlc.tipslab.utils.FirebaseClient
import kotlinx.coroutines.tasks.await

class LifehackRepository {
    private val db = FirebaseClient.db
    private val auth = FirebaseClient.auth
    private val mapper = LifehackMapper()

    suspend fun getMyLifehacks(): Result<List<Lifehack>> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("No autenticado"))

            val snapshot = db.collection("lifehacks")
                .whereEqualTo("author_id", uid)
                .get()
                .await()

            val lifehacks = snapshot.documents.mapNotNull { doc ->
                val dto = doc.toObject(LifehackDto::class.java)
                dto?.let { mapper.toDomain(it) }
            }

            Result.success(lifehacks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}