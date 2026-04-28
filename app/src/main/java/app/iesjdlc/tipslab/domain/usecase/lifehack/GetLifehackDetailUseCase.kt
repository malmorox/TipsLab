package app.iesjdlc.tipslab.domain.usecase.lifehack

import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.repository.SavedLikedRepository
import javax.inject.Inject

data class LifehackDetail(
    val lifehack: Lifehack,
    val isAuthor: Boolean,
    val isLiked: Boolean,
    val isSaved: Boolean
)

class GetLifehackDetailUseCase @Inject constructor(
    private val lifehackRepository: LifehackRepository,
    private val savedLikedRepository: SavedLikedRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(lifehackId: String): Result<LifehackDetail> = runCatching {
        val currentUser = authRepository.getCurrentUser()

        val lifehack = lifehackRepository.getLifehackById(lifehackId).getOrThrow()
        val isAuthor = lifehack.author.id == currentUser.id

        var isLiked = false
        var isSaved = false

        if (!isAuthor) {
            isSaved = savedLikedRepository.isLifehackSaved(currentUser.id, lifehackId).getOrDefault(false)
            isLiked = savedLikedRepository.isLifehackLiked(currentUser.id, lifehackId).getOrDefault(false)
        }

        LifehackDetail(
            lifehack = lifehack,
            isAuthor = isAuthor,
            isLiked = isLiked,
            isSaved = isSaved
        )
    }
}