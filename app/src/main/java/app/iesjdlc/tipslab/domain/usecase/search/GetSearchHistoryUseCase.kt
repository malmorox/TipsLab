package app.iesjdlc.tipslab.domain.usecase.search

import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.SearchRepository
import javax.inject.Inject

class GetSearchHistoryUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<List<String>> {
        val currentUser = authRepository.getCurrentUser()
        return searchRepository.getSearchHistory(currentUser.id)
    }
}