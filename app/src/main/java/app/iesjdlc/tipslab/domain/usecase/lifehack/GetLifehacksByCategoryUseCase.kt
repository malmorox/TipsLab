package app.iesjdlc.tipslab.domain.usecase.lifehack

import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import javax.inject.Inject

class GetLifehacksByCategoryUseCase @Inject constructor(
    private val lifehackRepository: LifehackRepository
) {
    suspend operator fun invoke(categoryId: Int): Result<List<Lifehack>> {
        return lifehackRepository.getLifehacksByCategory(categoryId)
    }
}