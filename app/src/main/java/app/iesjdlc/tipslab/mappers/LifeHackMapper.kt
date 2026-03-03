package app.iesjdlc.tipslab.mappers

import app.iesjdlc.tipslab.models.domain.Category
import app.iesjdlc.tipslab.models.domain.Lifehack
import app.iesjdlc.tipslab.models.domain.MediaType
import app.iesjdlc.tipslab.models.dto.LifehackDto

fun LifehackDto.toDomain(category: Category) = Lifehack(
    id = this.id,
    title = this.title,
    description = this.description,
    category = category,
    mediaType = MediaType.valueOf(this.media_type),
    mediaUrl = this.media_url,
)