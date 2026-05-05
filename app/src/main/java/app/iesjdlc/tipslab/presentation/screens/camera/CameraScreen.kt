package app.iesjdlc.tipslab.presentation.screens.camera

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import app.iesjdlc.tipslab.domain.model.MediaType
import kotlinx.coroutines.guava.await
import java.io.File

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel(),
    onMediaCaptured: (Uri, MediaType) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val hasPermission by viewModel.hasPermission.collectAsState()
    val isRecording by viewModel.isRecording.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onPermissionResult(granted)
        if (!granted) onNavigateBack()
    }

    LaunchedEffect(Unit) {
        viewModel.checkPermission()
        if (!hasPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is CameraEvent.MediaCaptured -> onMediaCaptured(event.uri, event.mediaType)
                is CameraEvent.Error -> { /* aquí puedes mostrar un Snackbar si tienes uno */ }
            }
        }
    }

    if (hasPermission) {
        CameraScreenUI(
            allowVideo = viewModel.allowVideo,
            isRecording = isRecording,
            lifecycleOwner = lifecycleOwner,
            imageCapture = viewModel.imageCapture,
            onVideoCaptureReady = { viewModel.onVideoCaptureReady(it) },
            onEvent = { event ->
                when (event) {
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
    allowVideo: Boolean,
    isRecording: Boolean,
    lifecycleOwner: LifecycleOwner,
    imageCapture: ImageCapture,
    onVideoCaptureReady: (VideoCapture<Recorder>) -> Unit,
    onEvent: (CameraControlsEvent) -> Unit,
) {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }

    LaunchedEffect(Unit) {
        try {
            val cameraProvider = ProcessCameraProvider.getInstance(context).await()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
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
            isRecording = isRecording,
            onEvent = onEvent,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}