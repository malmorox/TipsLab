package app.iesjdlc.tipslab.data.worker

import android.content.Context
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import app.iesjdlc.tipslab.core.constants.WorkerConstants
import app.iesjdlc.tipslab.domain.model.MediaType
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.repository.MediaRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UploadLifehackMediaWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val mediaRepository: MediaRepository,
    private val lifehackRepository: LifehackRepository
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val lifehackId = inputData.getString(WorkerConstants.LIFEHACK_ID_KEY) ?: return Result.failure()
        val mediaUriString = inputData.getString(WorkerConstants.LIFEHACK_MEDIA_URI) ?: return Result.failure()
        val mediaTypeString = inputData.getString(WorkerConstants.LIFEHACK_MEDIA_TYPE) ?: return Result.failure()

        val mediaUri = mediaUriString.toUri()
        val mediaType = MediaType.valueOf(mediaTypeString)

        val uploadResult = mediaRepository.uploadMediaToLifehack(
            lifehackId = lifehackId,
            mediaUri = mediaUri,
            mediaType = mediaType
        )

        if (uploadResult.isFailure) {
            // Reintentar hasta 3 veces ante fallos de red
            return if (runAttemptCount < 3) Result.retry()
            else Result.failure()
        }

        val mediaUrl = uploadResult.getOrNull() ?: return Result.failure()

        val updateResult = lifehackRepository.updateLifehackMedia(
            lifehackId = lifehackId,
            mediaUrl = mediaUrl,
            mediaType = mediaType
        )

        return if (updateResult.isSuccess) {
            Result.success(workDataOf(WorkerConstants.LIFEHACK_MEDIA_URL to mediaUrl))
        } else {
            val error = updateResult.exceptionOrNull()?.message ?: "Update failed"
            Result.failure()
        }
    }
}