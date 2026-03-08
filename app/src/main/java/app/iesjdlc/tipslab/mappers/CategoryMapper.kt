package app.iesjdlc.tipslab.mappers

import app.iesjdlc.tipslab.models.domain.Category
import app.iesjdlc.tipslab.models.dto.CategoryDto

class CategoryMapper {
    fun toDomain(dto: CategoryDto) = Category(
        id = dto.id,
        name = dto.name,
    )

    fun toDto(domain: Category) = CategoryDto(
        id = domain.id,
        name = domain.name,
    )
}