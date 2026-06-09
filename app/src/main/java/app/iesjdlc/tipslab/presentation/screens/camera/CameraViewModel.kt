package app.iesjdlc.tipslab.presentation.screens.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import app.iesjdlc.tipslab.domain.model.CameraMode
import app.iesjdlc.tipslab.domain.model.MediaType
import app.iesjdlc.tipslab.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @param:ApplicationContext private val context: Context,
) : ViewModel() {
    val allowVideo = savedStateHandle.toRoute<Route.Camera>().allowVideo

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    private val _hasPermission = MutableStateFlow(false)
    val hasPermission: StateFlow<Boolean> = _hasPermission.asStateFlow()

    private val _hasAudioPermission = MutableStateFlow(false)

    fun onPermissionResult(granted: Boolean) {
        _hasPermission.value = granted
    }

    fun onAudioPermissionResult(granted: Boolean) {
        _hasAudioPermission.value = granted
    }

    fun checkPermission() {
        _hasPermission.value = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        _hasAudioPermission.value = ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val _events = Channel<CameraEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    val imageCapture = ImageCapture.Builder().build()
    var videoCapture: VideoCapture<Recorder>? = null
        private set
    private var activeRecording: Recording? = null

    fun toggleMode() {
        if (_uiState.value.isRecording) return
        _uiState.update {
            it.copy(mode = if (it.mode == CameraMode.PHOTO) CameraMode.VIDEO else CameraMode.PHOTO)
        }
    }

    fun onVideoCaptureReady(videoCapture: VideoCapture<Recorder>) {
        this.videoCapture = videoCapture
    }

    fun capturePhoto() {
        Log.d("CameraViewModel", "capturePhoto() llamado")
        val file = File.createTempFile("photo_", ".jpg", context.cacheDir)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Log.d("CameraViewModel", "foto guardada: ${Uri.fromFile(file)}")
                    sendEvent(CameraEvent.MediaCaptured(Uri.fromFile(file), MediaType.IMAGE))
                }
                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraViewModel", "error foto: ${exception.message}")
                    sendEvent(CameraEvent.Error(exception.message ?: "Error al capturar foto"))
                }
            }
        )
    }

    fun startRecording() {
        if (_uiState.value.isRecording) return
        val vc = videoCapture ?: return
        try {
            val file = File.createTempFile("video_", ".mp4", context.cacheDir)
            val outputOptions = FileOutputOptions.Builder(file).build()
            val pendingRecording = vc.output.prepareRecording(context, outputOptions)

            if (_hasAudioPermission.value) pendingRecording.withAudioEnabled()

            activeRecording = pendingRecording
                .start(ContextCompat.getMainExecutor(context)) { recordEvent ->
                    when (recordEvent) {
                        is VideoRecordEvent.Start ->
                            _uiState.update { it.copy(isRecording = true) }
                        is VideoRecordEvent.Finalize -> {
                            _uiState.update { it.copy(isRecording = false) }
                            activeRecording = null
                            if (!recordEvent.hasError()) {
                                sendEvent(CameraEvent.MediaCaptured(
                                    recordEvent.outputResults.outputUri,
                                    MediaType.VIDEO
                                ))
                            } else {
                                sendEvent(CameraEvent.Error("Error al grabar vídeo"))
                            }
                        }
                    }
                }
        } catch (e: SecurityException) {
            sendEvent(CameraEvent.Error("Permiso de micrófono denegado"))
        }
    }

    fun stopRecording() {
        activeRecording?.stop()
        activeRecording = null
    }

    fun flipCamera() {
        if (_uiState.value.isRecording) return
        _uiState.update { it.copy(isFrontCamera = !it.isFrontCamera) }
    }

    private fun sendEvent(event: CameraEvent) {
        Log.d("CameraViewModel", "sendEvent: $event")
        viewModelScope.launch { _events.send(event) }
    }

    override fun onCleared() {
        super.onCleared()
        activeRecording?.stop()
    }
}
