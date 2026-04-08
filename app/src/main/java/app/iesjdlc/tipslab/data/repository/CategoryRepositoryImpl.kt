package app.iesjdlc.tipslab.data.repository

import app.iesjdlc.tipslab.data.mapper.CategoryMapper
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.data.model.CategoryDto
import app.iesjdlc.tipslab.data.remote.firebase.FirebaseClient
import app.iesjdlc.tipslab.domain.repository.CategoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CategoryRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseClient.db,
    private val mapper: CategoryMapper = CategoryMapper()
) : CategoryRepository {
    suspend fun getAllCategories(): Result<List<Category>> {
        return try {
            val snapshot = db.collection("categories").get().await()

            val categories = snapshot.documents.mapNotNull { doc ->
                doc.toObject(CategoryDto::class.java)
                    ?.let { mapper.toDomain(it) }
            }

            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCategoryById(id: String): Result<Category> {
        return try {
            val doc = db.collection("categories").document(id).get().await()
            doc.toObject(CategoryDto::class.java)
                ?.let { Result.success(mapper.toDomain(it)) }
                ?: Result.failure(Exception("Categoría no encontrada"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}