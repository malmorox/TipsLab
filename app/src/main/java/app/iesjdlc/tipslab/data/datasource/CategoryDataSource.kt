package app.iesjdlc.tipslab.data.datasource

import app.iesjdlc.tipslab.data.model.CategoryDto

interface CategoryDataSource {
    fun getCategories(): List<CategoryDto>
    fun getById(id: Int): CategoryDto?
    fun getCategoriesByIds(ids: List<Int>): Map<Int, CategoryDto>
}