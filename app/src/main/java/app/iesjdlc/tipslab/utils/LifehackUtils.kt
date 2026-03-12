package app.iesjdlc.tipslab.utils

import app.iesjdlc.tipslab.mappers.LifehackMapper
import app.iesjdlc.tipslab.models.domain.Lifehack
import app.iesjdlc.tipslab.models.dto.LifehackDto
import app.iesjdlc.tipslab.repository.CategoryRepository
import app.iesjdlc.tipslab.repository.UserRepository

class LifehackUtils(
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val mapper: LifehackMapper = LifehackMapper()
) {
    suspend fun mapToLifehack(dto: LifehackDto): Lifehack? {
        val category = categoryRepository.getCategoryById(dto.category_id).getOrNull() ?: return null
        val author = userRepository.getUserById(dto.author_id).getOrNull() ?: return null
        return mapper.toDomain(dto, category, author)
    }
}