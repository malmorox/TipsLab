package app.iesjdlc.tipslab.data.mapper

import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.data.model.CategoryDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryMapper @Inject constructor() {
    fun toDomain(dto: CategoryDto) = Category(
        id = dto.id,
        name = dto.name,
        description = dto.description
    )
}