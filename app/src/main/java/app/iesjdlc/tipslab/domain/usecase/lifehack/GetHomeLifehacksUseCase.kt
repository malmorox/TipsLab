package app.iesjdlc.tipslab.domain.usecase.lifehack

import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.model.screen_sections.HomeSection
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.repository.OrderBy
import javax.inject.Inject

class GetHomeLifehacksUseCase @Inject constructor(
    private val lifehackRepository: LifehackRepository
) {
    suspend operator fun invoke(section: HomeSection, limit: Int = 10): Result<List<Lifehack>> =
        when (section) {
            HomeSection.RECENT -> lifehackRepository.getLifehacks(OrderBy.RECENT, limit)
            HomeSection.TRENDING -> lifehackRepository.getLifehacks(OrderBy.POPULAR, limit)
            HomeSection.FOR_YOU -> lifehackRepository.getRandomLifehacks(limit)
        }
}