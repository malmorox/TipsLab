package app.iesjdlc.tipslab.domain.usecase.search

import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.SearchRepository
import javax.inject.Inject

class SaveSearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(query: String): Result<Unit> {
        if (query.isBlank()) return Result.success(Unit)
        val currentUser = authRepository.getCurrentUser()
        return searchRepository.saveSearch(currentUser.id, query)
    }
}