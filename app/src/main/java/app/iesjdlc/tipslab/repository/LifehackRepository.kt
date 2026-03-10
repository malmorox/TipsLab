package app.iesjdlc.tipslab.repository

import android.util.Log
import app.iesjdlc.tipslab.mappers.LifehackMapper
import app.iesjdlc.tipslab.models.domain.Lifehack
import app.iesjdlc.tipslab.models.dto.LifehackDto
import app.iesjdlc.tipslab.utils.FirebaseClient
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

class LifehackRepository {
    private val db = FirebaseClient.db
    private val auth = FirebaseClient.auth
    // Quitar estas dependencias y utilizar use cases para obtener la información necesaria
    private val categoryRepository = CategoryRepository()
    private val userRepository = UserRepository()
    private val mapper = LifehackMapper()

    suspend fun getMyLifehacks(): Result<List<Lifehack>> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("No autenticado"))
            val snapshot = db.collection("lifehacks")
                .whereEqualTo("author_id", uid)
                .get().await()
            Result.success(snapshot.documents.mapNotNull { doc -> mapDoc(doc) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLifehackById(id: String): Result<Lifehack> {
        return try {
            val doc = db.collection("lifehacks").document(id).get().await()
            mapDoc(doc)?.let { Result.success(it) }
                ?: Result.failure(Exception("Lifehack no encontrado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLifehacksByCategory(categoryId: String): Result<List<Lifehack>> {
        return try {
            val snapshot = db.collection("lifehacks")
                .whereEqualTo("category_id", categoryId)
                .get().await()
            Result.success(snapshot.documents.mapNotNull { doc -> mapDoc(doc) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRandomLifehacks(limit: Int = 10): Result<List<Lifehack>> {
        return Result.failure(Exception("Funcionalidad no implementada"))
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

    suspend fun deleteLifehack(id: String): Result<Unit> {
        return try {
            db.collection("lifehacks").document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    private suspend fun mapDoc(doc: DocumentSnapshot): Lifehack? {
        val dto = doc.toObject(LifehackDto::class.java) ?: return null
        val category = categoryRepository.getCategoryById(dto.category_id).getOrNull() ?: return null
        val author = userRepository.getUserById(dto.author_id).getOrNull() ?: return null
        return mapper.toDomain(dto, category, author)
    }
}