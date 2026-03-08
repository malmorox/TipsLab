package app.iesjdlc.tipslab.repository

import app.iesjdlc.tipslab.mappers.CategoryMapper
import app.iesjdlc.tipslab.models.domain.Category
import app.iesjdlc.tipslab.models.dto.CategoryDto
import app.iesjdlc.tipslab.utils.FirebaseClient
import kotlinx.coroutines.tasks.await

class CategoryRepository {
    private val db = FirebaseClient.db
    private val mapper = CategoryMapper()

    suspend fun getCategoryById(id: String): Category {
        val doc = db.collection("categories").document(id).get().await()
        return mapper.toDomain(
            doc.toObject(CategoryDto::class.java) ?: throw Exception("Categoría no encontrada")
        )
    }
}