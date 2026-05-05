package app.iesjdlc.tipslab.presentation.screens.camera

sealed class CameraControlsEvent {
    data object CapturePhoto : CameraControlsEvent()
    data object StartRecording : CameraControlsEvent()
    data object StopRecording : CameraControlsEvent()
    data object FlipCamera : CameraControlsEvent()
    data object NavigateBack : CameraControlsEvent()
}