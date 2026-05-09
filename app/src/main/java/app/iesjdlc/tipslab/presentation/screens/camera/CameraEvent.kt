package app.iesjdlc.tipslab.presentation.screens.camera

import android.net.Uri
import app.iesjdlc.tipslab.domain.model.MediaType

sealed class CameraEvent {
    data class MediaCaptured(val uri: Uri, val mediaType: MediaType) : CameraEvent()
    data class Error(val message: String) : CameraEvent()
}