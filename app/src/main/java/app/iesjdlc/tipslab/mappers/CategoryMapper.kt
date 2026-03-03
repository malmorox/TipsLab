package app.iesjdlc.tipslab.mappers

import app.iesjdlc.tipslab.models.domain.Category
import app.iesjdlc.tipslab.models.dto.CategoryDto

fun CategoryDto.toDomain() = Category(
    id = this.id,
    name = this.name,
)