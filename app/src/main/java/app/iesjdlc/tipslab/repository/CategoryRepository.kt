package app.iesjdlc.tipslab.repository

import android.util.Log
import app.iesjdlc.tipslab.mappers.CategoryMapper
import app.iesjdlc.tipslab.models.domain.Category
import app.iesjdlc.tipslab.models.dto.CategoryDto
import app.iesjdlc.tipslab.utils.FirebaseClient
import kotlinx.coroutines.tasks.await

class CategoryRepository {
    private val db = FirebaseClient.db
    private val mapper = CategoryMapper()

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