package app.iesjdlc.tipslab.domain.usecase

import android.net.Uri
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.model.MediaType
import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.repository.MediaRepository
import javax.inject.Inject

class CreateLifehackUseCase @Inject constructor(
    private val lifehackRepository: LifehackRepository,
    private val mediaRepository: MediaRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        category: Category,
        mediaUri: Uri?,
        mediaType: MediaType?,
    ): Result<String> {
        /*val currentUser =


        val lifehack = Lifehack(
            id = "",
            title = title,
            description = description,
            category = category,
            author = currentUser,
            media = media,
            likedCount = 0,
        )

        return lifehackRepository.createLifehack(lifehack)*/
        return Result.failure(NotImplementedError("CreateLifehackUseCase is not implemented yet"))
    }
}