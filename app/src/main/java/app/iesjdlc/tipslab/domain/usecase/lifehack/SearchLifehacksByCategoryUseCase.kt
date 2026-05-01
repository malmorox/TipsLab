package app.iesjdlc.tipslab.domain.usecase.lifehack

import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.repository.SearchRepository
import javax.inject.Inject

class SearchLifehacksByCategoryUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(
        categoryId: Int,
        query: String,
        limit: Int = 5,
        offset: Int = 0
    ): Result<List<Lifehack>> {
        return searchRepository.searchLifehacksByCategory(
            categoryId,
            query,
            limit,
            offset
        )
    }
}