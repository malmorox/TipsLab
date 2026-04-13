package app.iesjdlc.tipslab.data.mapper

import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.model.User
import app.iesjdlc.tipslab.data.model.LifehackDto
import app.iesjdlc.tipslab.domain.model.Media
import app.iesjdlc.tipslab.domain.model.MediaType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LifehackMapper @Inject constructor() {
    fun toDomain(dto: LifehackDto, category: Category, author: User, ): Lifehack {
        return Lifehack(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            category = category,
            author = author,
            media = dto.mapMedia(),
            likedCount = dto.liked_count,
        )
    }

    fun toDto(domain: Lifehack) = LifehackDto(
        id = domain.id,
        title = domain.title,
        description = domain.description,
        category_id = domain.category.id,
        author_id = domain.author.id,
        media_type = domain.media?.type?.name,
        media_url = domain.media?.url,
        liked_count = domain.likedCount,
    )

    private fun LifehackDto.mapMedia(): Media? {
        val type = media_type
            ?.let { runCatching { MediaType.valueOf(it) }.getOrNull() }
            ?: return null
        val url = media_url ?: return null
        return Media(type = type, url = url)
    }
}