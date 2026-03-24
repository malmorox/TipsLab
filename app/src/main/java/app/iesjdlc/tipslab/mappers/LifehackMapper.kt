package app.iesjdlc.tipslab.mappers

import app.iesjdlc.tipslab.models.domain.Category
import app.iesjdlc.tipslab.models.domain.Lifehack
import app.iesjdlc.tipslab.models.domain.MediaType
import app.iesjdlc.tipslab.models.domain.User
import app.iesjdlc.tipslab.models.dto.LifehackDto

class LifehackMapper {
    fun toDomain(dto: LifehackDto, category: Category, author: User): Lifehack {
        return Lifehack(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            category = category,
            author = author,
            mediaType = dto.media_type?.let {
                runCatching { MediaType.valueOf(it.uppercase()) }.getOrNull()
            },
            mediaUrl = dto.media_url,
            savedCount = dto.saved_count,
        )
    }

    fun toDto(domain: Lifehack) = LifehackDto(
        id = domain.id,
        title = domain.title,
        description = domain.description,
        category_id = domain.category.id,
        author_id = domain.author.id,
        media_type = domain.mediaType?.name,
        media_url = domain.mediaUrl,
        saved_count = domain.savedCount,
    )
}