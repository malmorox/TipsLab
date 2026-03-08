package app.iesjdlc.tipslab.mappers

import app.iesjdlc.tipslab.models.domain.Lifehack
import app.iesjdlc.tipslab.models.domain.MediaType
import app.iesjdlc.tipslab.models.dto.LifehackDto
import app.iesjdlc.tipslab.repository.CategoryRepository
import app.iesjdlc.tipslab.repository.UserRepository

class LifehackMapper(
    private val categoryRepository: CategoryRepository = CategoryRepository(),
    private val userRepository: UserRepository = UserRepository()
) {
    suspend fun toDomain(dto: LifehackDto): Lifehack {
        val category = categoryRepository.getCategoryById(dto.category_id)
        val author = userRepository.getUserById(dto.author_id)

        return Lifehack(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            category = category,
            author = author,
            mediaType = MediaType.valueOf(dto.media_type),
            mediaUrl = dto.media_url,
        )
    }

    fun toDto(domain: Lifehack) = LifehackDto(
        id = domain.id,
        title = domain.title,
        description = domain.description,
        category_id = domain.category.id,
        author_id = domain.author.id,
        media_type = domain.mediaType.name,
        media_url = domain.mediaUrl,
    )
}