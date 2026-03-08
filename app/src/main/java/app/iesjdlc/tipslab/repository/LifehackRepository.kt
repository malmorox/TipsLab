package app.iesjdlc.tipslab.repository

import android.util.Log
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

    suspend fun getLifehackById(id: String): Result<Lifehack> {
        return try {
            val doc = db.collection("lifehacks").document(id).get().await()
            val dto = doc.toObject(LifehackDto::class.java)
            dto?.let { Result.success(mapper.toDomain(it)) }
                ?: Result.failure(Exception("Lifehack no encontrado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createLifehack(lifehack: Lifehack): Result<String> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("No autenticado"))
            val dto = mapper.toDto(lifehack.copy(author = lifehack.author.copy(id = uid)))
            val docRef = db.collection("lifehacks").add(dto).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

        suspend fun updateLifehack(id: String, lifehack: Lifehack): Result<Unit> {
            return try {
                val uid = auth.currentUser?.uid ?: return Result.failure(Exception("No autenticado"))
                val dto = mapper.toDto(lifehack.copy(author = lifehack.author.copy(id = uid)))
                db.collection("lifehacks").document(id).set(dto).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}