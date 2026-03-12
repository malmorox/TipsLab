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

    suspend fun getMyLifehacks(): Result<List<LifehackDto>> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("No autenticado"))
            val snapshot = db.collection("lifehacks")
                .whereEqualTo("author_id", uid)
                .get().await()
            Result.success(snapshot.documents.mapNotNull { doc -> doc.toObject(LifehackDto::class.java) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLifehackById(id: String): Result<LifehackDto> {
        return try {
            val doc = db.collection("lifehacks").document(id).get().await()
            doc.toObject(LifehackDto::class.java)
                ?.let { Result.success(it) }
                ?: Result.failure(Exception("Lifehack no encontrado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLifehacksByCategory(categoryId: String): Result<List<LifehackDto>> {
        return try {
            val snapshot = db.collection("lifehacks")
                .whereEqualTo("category_id", categoryId)
                .get().await()
            Result.success(snapshot.documents.mapNotNull { doc -> doc.toObject(LifehackDto::class.java) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRandomLifehacks(limit: Int = 10): Result<List<LifehackDto>> {
        return Result.failure(Exception("Funcionalidad no implementada"))
    }

    suspend fun createLifehack(lifehack: Lifehack): Result<String> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("No autenticado"))
            val dto = mapper.toDto(lifehack)
            val docRef = db.collection("lifehacks").add(dto).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateLifehack(id: String, lifehack: Lifehack): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("No autenticado"))
            val dto = mapper.toDto(lifehack)
            db.collection("lifehacks").document(id).set(dto).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteLifehack(id: String): Result<Unit> {
        return try {
            db.collection("lifehacks").document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}