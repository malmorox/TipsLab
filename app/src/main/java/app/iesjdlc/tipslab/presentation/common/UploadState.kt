package app.iesjdlc.tipslab.presentation.common

sealed class UploadState {
    object Loading : UploadState()
    object Success : UploadState()
    object Error : UploadState()
}
