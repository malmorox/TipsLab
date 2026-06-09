package app.iesjdlc.tipslab.domain.usecase.lifehack

import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.repository.SavedLikedRepository
import javax.inject.Inject

data class LifehackDetail(
    val lifehack: Lifehack,
    val isAuthor: Boolean
)

class GetLifehackDetailUseCase @Inject constructor(
    private val lifehackRepository: LifehackRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(lifehackId: String): Result<LifehackDetail> = runCatching {
        val currentUser = authRepository.getCurrentUser()

        val lifehack = lifehackRepository.getLifehackById(lifehackId).getOrThrow()
        val isAuthor = lifehack.author.id == currentUser.id

        LifehackDetail(
            lifehack = lifehack,
            isAuthor = isAuthor
        )
    }
}