package app.iesjdlc.tipslab.domain.usecase.lifehack

import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.SavedLikedRepository
import javax.inject.Inject

class ToggleSaveLifehackUseCase @Inject constructor(
    private val savedLikedRepository: SavedLikedRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(lifehackId: String): Result<Boolean> {
        val currentUser = authRepository.getCurrentUser()
        return savedLikedRepository.toggleSaved(currentUser.id, lifehackId)
    }
}