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

class EditLifehackUseCase @Inject constructor(
    private val lifehackRepository: LifehackRepository,
    private val mediaRepository: MediaRepository,
    private val authRepository: AuthRepository
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

        return Result.success("")
    }
}