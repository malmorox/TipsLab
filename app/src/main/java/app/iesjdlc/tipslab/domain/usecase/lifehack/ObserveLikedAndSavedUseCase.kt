package app.iesjdlc.tipslab.domain.usecase.lifehack

import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.SavedLikedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveLikedAndSavedUseCase @Inject constructor(
    private val savedLikedRepository: SavedLikedRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(lifehackId: String): Flow<Pair<Boolean, Boolean>> {
        val currentUser = authRepository.getCurrentUser()
        return combine(
            savedLikedRepository.observeIsLiked(currentUser.id, lifehackId),
            savedLikedRepository.observeIsSaved(currentUser.id, lifehackId)
        ) { isLiked, isSaved -> Pair(isLiked, isSaved) }
    }
}