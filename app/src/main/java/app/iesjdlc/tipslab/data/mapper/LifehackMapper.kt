package app.iesjdlc.tipslab.data.mapper

import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.model.MediaType
import app.iesjdlc.tipslab.domain.model.User
import app.iesjdlc.tipslab.data.model.LifehackDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LifehackMapper @Inject constructor() {
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