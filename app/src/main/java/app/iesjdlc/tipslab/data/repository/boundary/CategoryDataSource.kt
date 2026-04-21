package app.iesjdlc.tipslab.data.repository.boundary

import app.iesjdlc.tipslab.data.model.CategoryDto

interface CategoryDataSource {
    fun getCategories(): List<CategoryDto>
    fun getById(id: String): CategoryDto?
}