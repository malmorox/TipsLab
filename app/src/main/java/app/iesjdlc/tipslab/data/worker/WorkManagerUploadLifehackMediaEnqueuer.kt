package app.iesjdlc.tipslab.data.worker

import android.content.Context
import android.net.Uri
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import app.iesjdlc.tipslab.core.constants.WorkerConstants
import app.iesjdlc.tipslab.domain.model.MediaType
import app.iesjdlc.tipslab.domain.usecase.lifehack.boundary.UploadLifehackMediaEnqueuer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WorkManagerUploadLifehackMediaEnqueuer @Inject constructor(
    @param:ApplicationContext private val context: Context
) : UploadLifehackMediaEnqueuer {

    override fun enqueue(
        lifehackId: String,
        mediaUri: Uri,
        mediaType: MediaType
    ) {
        val inputData = workDataOf(
            WorkerConstants.LIFEHACK_ID_KEY to lifehackId,
            WorkerConstants.LIFEHACK_MEDIA_URI to mediaUri.toString(),
            WorkerConstants.LIFEHACK_MEDIA_TYPE to mediaType.name
        )

        val request = OneTimeWorkRequestBuilder<UploadLifehackMediaWorker>()
            .setInputData(inputData)
            .addTag(lifehackId)
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }
}