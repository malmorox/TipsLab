package app.iesjdlc.tipslab.presentation.screens.camera

import app.iesjdlc.tipslab.domain.model.CameraMode

data class CameraUiState(
    val isRecording: Boolean = false,
    val isFrontCamera: Boolean = false,
    val mode: CameraMode = CameraMode.PHOTO
)