package app.iesjdlc.tipslab.presentation.screens.camera

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import app.iesjdlc.tipslab.domain.model.MediaType
import kotlinx.coroutines.guava.await

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel(),
    onMediaCaptured: (Uri, MediaType) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsState()
    val hasPermission by viewModel.hasPermission.collectAsState()

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onPermissionResult(granted)
        if (!granted) onNavigateBack()
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onAudioPermissionResult(granted)
    }

    LaunchedEffect(Unit) {
        viewModel.checkPermission()
        if (viewModel.allowVideo) audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    LaunchedEffect(hasPermission) {
        if (!hasPermission) cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    LaunchedEffect(Unit) {
        viewModel.checkPermission()
        if (viewModel.allowVideo) audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    LaunchedEffect("events") {
        viewModel.events.collect { event ->
            when (event) {
                is CameraEvent.MediaCaptured -> onMediaCaptured(event.uri, event.mediaType)
                is CameraEvent.Error -> { /* aquí puedes mostrar un Snackbar si tienes uno */ }
            }
        }
    }

    if (hasPermission) {
        CameraScreenUI(
            state = uiState,
            allowVideo = viewModel.allowVideo,
            lifecycleOwner = lifecycleOwner,
            imageCapture = viewModel.imageCapture,
            onVideoCaptureReady = { viewModel.onVideoCaptureReady(it) },
            onEvent = { event ->
                when (event) {
                    is CameraControlsEvent.ToggleMode -> viewModel.toggleMode()
                    is CameraControlsEvent.CapturePhoto -> viewModel.capturePhoto()
                    is CameraControlsEvent.StartRecording -> viewModel.startRecording()
                    is CameraControlsEvent.StopRecording -> viewModel.stopRecording()
                    is CameraControlsEvent.FlipCamera -> viewModel.flipCamera()
                    is CameraControlsEvent.NavigateBack -> onNavigateBack()
                }
            }
        )
    }
}

@Composable
private fun CameraScreenUI(
    state: CameraUiState,
    allowVideo: Boolean,
    lifecycleOwner: LifecycleOwner,
    imageCapture: ImageCapture,
    onVideoCaptureReady: (VideoCapture<Recorder>) -> Unit,
    onEvent: (CameraControlsEvent) -> Unit
) {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }

    val cameraSelector = if (state.isFrontCamera)
        CameraSelector.DEFAULT_FRONT_CAMERA
    else
        CameraSelector.DEFAULT_BACK_CAMERA

    key(cameraSelector) {
        LaunchedEffect(Unit) {
            try {
                val cameraProvider = ProcessCameraProvider.getInstance(context).await()
                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }
                cameraProvider.unbindAll()

                if (allowVideo) {
                    val recorder = Recorder.Builder()
                        .setQualitySelector(QualitySelector.from(Quality.HD))
                        .build()
                    val videoCapture = VideoCapture.withOutput(recorder)
                    onVideoCaptureReady(videoCapture)
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, imageCapture, videoCapture
                    )
                } else {
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, imageCapture
                    )
                }
            } catch (e: SecurityException) {
                onEvent(CameraControlsEvent.NavigateBack)
            } catch (e: Exception) {
                onEvent(CameraControlsEvent.NavigateBack)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        CameraControls(
            allowVideo = allowVideo,
            isRecording = state.isRecording,
            mode = state.mode,
            onEvent = onEvent
        )
    }
}