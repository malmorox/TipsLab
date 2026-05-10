package app.iesjdlc.tipslab.domain.usecase.lifehack.boundary

import android.net.Uri
import app.iesjdlc.tipslab.domain.model.MediaType

interface UploadLifehackMediaEnqueuer {
    fun enqueue(
        lifehackId: String,
        mediaUri: Uri,
        mediaType: MediaType
    )
}