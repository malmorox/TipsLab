package app.iesjdlc.tipslab.repository

import app.iesjdlc.tipslab.mappers.LifehackMapper
import app.iesjdlc.tipslab.models.domain.Lifehack
import app.iesjdlc.tipslab.models.dto.LifehackDto
import app.iesjdlc.tipslab.utils.FirebaseClient
import app.iesjdlc.tipslab.utils.LifehackResolver
import kotlinx.coroutines.tasks.await

class LifehackRepository(
    private val resolver: LifehackResolver = LifehackResolver()
) {
    private val db = FirebaseClient.db
    private val auth = FirebaseClient.auth
    private val mapper = LifehackMapper()

    suspend fun getMyLifehacks(): Result<List<Lifehack>> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("No autenticado"))
            val snapshot = db.collection("lifehacks")
                .whereEqualTo("author_id", uid)
                .get().await()
            val dtos = snapshot.documents.mapNotNull { doc -> doc.toObject(LifehackDto::class.java) }
            Result.success(resolver.resolve(dtos))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLifehackById(id: String): Result<Lifehack> {
        return try {
            val snapshot = db.collection("lifehacks")
                .document(id)
                .get()
                .await()
            val dto = snapshot.toObject(LifehackDto::class.java)
                ?: return Result.failure(Exception("No encontrado"))
            Result.success(resolver.resolveOne(dto)
                ?: return Result.failure(Exception("No encontrado")))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLifehacksByCategory(categoryId: String): Result<List<Lifehack>> {
        return try {
            val snapshot = db.collection("lifehacks")
                .whereEqualTo("category_id", categoryId)
                .get()
                .await()
            val dtos = snapshot.documents.mapNotNull { doc -> doc.toObject(LifehackDto::class.java) }
            Result.success(resolver.resolve(dtos))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRandomLifehacks(limit: Int = 10): Result<List<Lifehack>> {
        return Result.failure(Exception("Funcionalidad no implementada"))
    }

    suspend fun createLifehack(lifehack: Lifehack): Result<String> {
        return try {
            val docRef = db.collection("lifehacks").document()
            val dto = mapper.toDto(lifehack).copy(id = docRef.id)
            docRef.set(dto).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateLifehack(id: String, lifehack: Lifehack): Result<Unit> {
        return try {
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