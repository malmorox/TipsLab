package app.iesjdlc.tipslab.data.repository

import app.iesjdlc.tipslab.data.mapper.LifehackMapper
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.data.model.LifehackDto
import app.iesjdlc.tipslab.data.remote.firebase.FirebaseClient
import app.iesjdlc.tipslab.data.resolver.LifehackResolver
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LifehackRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseClient.db,
    private val auth: FirebaseAuth = FirebaseClient.auth,
    private val mapper: LifehackMapper = LifehackMapper(),
    private val resolver: LifehackResolver = LifehackResolver()
) : LifehackRepository {
    override suspend fun getMyLifehacks(): Result<List<Lifehack>> {
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

    override suspend fun getLifehackById(id: String): Result<Lifehack> {
        return try {
            val snapshot = db.collection("lifehacks")
                .document(id)
                .get()
                .await()
            val dto = snapshot.toObject(LifehackDto::class.java)
            if (dto != null) {
                Result.success(resolver.resolveOne(dto)!!)
            } else {
                Result.failure(Exception("Lifehack no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLifehacksByCategory(categoryId: String): Result<List<Lifehack>> {
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

    override suspend fun getRandomLifehacks(limit: Int): Result<List<Lifehack>> {
        return Result.failure(Exception("Funcionalidad no implementada"))
    }

    override suspend fun createLifehack(lifehack: Lifehack): Result<String> {
        return try {
            val docRef = db.collection("lifehacks").document()
            val dto = mapper.toDto(lifehack).copy(id = docRef.id)
            docRef.set(dto).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateLifehack(id: String, lifehack: Lifehack): Result<Unit> {
        return try {
            val dto = mapper.toDto(lifehack)
            db.collection("lifehacks").document(id).set(dto).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteLifehack(id: String): Result<Unit> {
        return try {
            db.collection("lifehacks").document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}