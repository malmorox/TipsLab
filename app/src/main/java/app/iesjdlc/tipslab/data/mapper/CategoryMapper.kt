package app.iesjdlc.tipslab.data.mapper

import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.data.model.CategoryDto

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