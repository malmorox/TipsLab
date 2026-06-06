package app.iesjdlc.tipslab.domain.usecase.lifehack

import android.net.Uri
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.MediaType
import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.usecase.lifehack.boundary.UploadLifehackMediaEnqueuer
import kotlinx.datetime.Clock
import javax.inject.Inject

class EditLifehackUseCase @Inject constructor(
    private val lifehackRepository: LifehackRepository,
    private val authRepository: AuthRepository,
    private val uploadMediaEnqueuer: UploadLifehackMediaEnqueuer
) {
    suspend operator fun invoke(
        id: String,
        title: String,
        description: String,
        steps: List<String>,
        category: Category,
        mediaUri: Uri?,
        mediaType: MediaType?
    ): Result<String> {
        val currentUser = authRepository.getCurrentUser()
        val existingResult = lifehackRepository.getLifehackById(id)

        return existingResult.fold(
            onSuccess = { existing ->
                val updated = existing.copy(
                    title = title,
                    description = description,
                    steps = steps,
                    category = category,
                    author = currentUser,
                    updatedAt = Clock.System.now()
                )

                lifehackRepository.updateLifehack(id, updated).fold(
                    onSuccess = {
                        if (mediaUri != null && mediaType != null) {
                            uploadMediaEnqueuer.enqueue(id, mediaUri, mediaType)
                        }
                        Result.success(id)
                    },
                    onFailure = { err -> Result.failure(err) }
                )
            },
            onFailure = { err -> Result.failure(err) }
        )
    }
}