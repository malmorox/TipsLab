package app.iesjdlc.tipslab.data.mapper

import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.model.User
import app.iesjdlc.tipslab.data.model.LifehackDto
import app.iesjdlc.tipslab.domain.model.Media
import app.iesjdlc.tipslab.domain.model.MediaType
import kotlinx.datetime.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LifehackMapper @Inject constructor() {
    fun toDomain(dto: LifehackDto, category: Category, author: User ): Lifehack {
        return Lifehack(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            steps = dto.steps,
            category = category,
            author = author,
            media = dto.mapMedia(),
            createdAt = Instant.fromEpochMilliseconds(dto.createdAt),
            updatedAt = Instant.fromEpochMilliseconds(dto.updatedAt),
            likesCount = dto.likesCount,
            commentsCount = dto.commentsCount
        )
    }

    fun toDto(domain: Lifehack) = LifehackDto(
        id = domain.id,
        title = domain.title,
        description = domain.description,
        steps = domain.steps,
        categoryId = domain.category.id,
        authorId = domain.author.id,
        mediaType = domain.media?.type?.name,
        mediaUrl = domain.media?.url,
        createdAt = domain.createdAt.toEpochMilliseconds(),
        updatedAt = domain.updatedAt.toEpochMilliseconds(),
        likesCount = domain.likesCount,
        commentsCount = domain.commentsCount
    )

    private fun LifehackDto.mapMedia(): Media? {
        val type = mediaType
            ?.let { runCatching { MediaType.valueOf(it) }.getOrNull() }
            ?: return null
        val url = mediaUrl ?: return null
        return Media(type = type, url = url)
    }
}