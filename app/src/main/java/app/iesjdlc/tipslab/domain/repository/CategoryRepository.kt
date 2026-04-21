package app.iesjdlc.tipslab.domain.repository

import app.iesjdlc.tipslab.domain.model.Category

interface CategoryRepository {
    fun getAllCategories(): Result<List<Category>>
    fun getCategoryById(id: String): Result<Category>
}