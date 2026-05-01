package app.iesjdlc.tipslab.domain.usecase.lifehack

import app.iesjdlc.tipslab.domain.model.CategorySection
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.repository.OrderBy
import javax.inject.Inject

class GetLifehacksByCategoryUseCase @Inject constructor(
    private val lifehackRepository: LifehackRepository
) {
    suspend operator fun invoke(
        categoryId: Int,
        section: CategorySection,
        limit: Int = 10,
        offset: Int = 0
    ): Result<List<Lifehack>> {
        return lifehackRepository.getLifehacksByCategory(
            categoryId = categoryId,
            orderBy = when (section) {
                CategorySection.RECENT -> OrderBy.RECENT
                CategorySection.POPULAR -> OrderBy.POPULAR
            },
            limit = limit,
            offset = offset
        )
    }
}