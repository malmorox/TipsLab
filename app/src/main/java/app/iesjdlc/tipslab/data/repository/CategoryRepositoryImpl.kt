package app.iesjdlc.tipslab.data.repository

import app.iesjdlc.tipslab.data.mapper.CategoryMapper
import app.iesjdlc.tipslab.data.repository.boundary.CategoryDataSource
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dataSource: CategoryDataSource,
    private val mapper: CategoryMapper
) : CategoryRepository {
    override fun getAllCategories(): Result<List<Category>> =
        runCatching {
            dataSource.getCategories().map { mapper.toDomain(it) }
        }

    override fun getCategoryById(id: String): Result<Category> =
        runCatching {
            dataSource.getById(id)
                ?.let { mapper.toDomain(it) }
                ?: error("Categoría no encontrada: $id")
        }
}