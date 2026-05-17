package app.iesjdlc.tipslab.domain.usecase.lifehack

import android.net.Uri
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.model.MediaType
import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.usecase.lifehack.boundary.UploadLifehackMediaEnqueuer
import kotlinx.datetime.Clock
import javax.inject.Inject

class CreateLifehackUseCase @Inject constructor(
    private val lifehackRepository: LifehackRepository,
    private val authRepository: AuthRepository,
    private val uploadMediaEnqueuer: UploadLifehackMediaEnqueuer
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        steps: List<String>,
        category: Category,
        mediaUri: Uri?,
        mediaType: MediaType?
    ): Result<String> {
        val currentUser = authRepository.getCurrentUser()

        val lifehack = Lifehack(
            id = "",
            title = title,
            description = description,
            steps = steps,
            category = category,
            author = currentUser,
            media = null,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
            likesCount = 0,
            commentsCount = 0
        )

        return lifehackRepository.createLifehack(lifehack)
            .fold(
                onSuccess = { lifehackId ->
                    if (mediaUri != null && mediaType != null) {
                        uploadMediaEnqueuer.enqueue(
                            lifehackId,
                            mediaUri,
                            mediaType
                        )
                    }
                    Result.success(lifehackId)
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )

    }
}