package app.iesjdlc.tipslab.domain.repository

import app.iesjdlc.tipslab.domain.model.Category

interface CategoryRepository {
    suspend fun getAllCategories(): Result<List<Category>>
    suspend fun getCategoryById(id: String): Result<Category>
}