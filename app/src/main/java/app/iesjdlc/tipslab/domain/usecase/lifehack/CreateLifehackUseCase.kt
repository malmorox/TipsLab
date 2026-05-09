package app.iesjdlc.tipslab.domain.usecase.lifehack

import android.net.Uri
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.model.MediaType
import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.repository.MediaRepository
import kotlinx.datetime.Clock
import javax.inject.Inject

class CreateLifehackUseCase @Inject constructor(
    private val lifehackRepository: LifehackRepository,
    private val mediaRepository: MediaRepository,
    private val authRepository: AuthRepository
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
        )

        return lifehackRepository.createLifehack(lifehack)
            .fold(
                onSuccess = { lifehackId ->
                    if (mediaUri != null) {
                        Result.success(lifehackId) // TODO subir imagen y actualizar lifehack
                    } else {
                        Result.success(lifehackId)
                    }
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )

    }
}