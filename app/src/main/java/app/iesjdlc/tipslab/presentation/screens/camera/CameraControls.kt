package app.iesjdlc.tipslab.presentation.screens.camera

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cameraswitch
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.domain.model.CameraMode

@Composable
fun CameraControls(
    allowVideo: Boolean,
    isRecording: Boolean,
    mode: CameraMode,
    onEvent: (CameraControlsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Botón cerrar
        IconButton(
            onClick = { onEvent(CameraControlsEvent.NavigateBack) },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = stringResource(R.string.close),
                tint = Color.White
            )
        }

        IconButton(
            onClick = { onEvent(CameraControlsEvent.FlipCamera) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Cameraswitch,
                contentDescription = stringResource(R.string.flip_camera),
                tint = Color.White
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {

            // Botón de captura
            when (mode) {
                CameraMode.PHOTO -> PhotoCaptureButton(
                    onClick = { onEvent(CameraControlsEvent.CapturePhoto) }
                )
                CameraMode.VIDEO -> VideoCaptureButton(
                    isRecording = isRecording,
                    onClick = {
                        if (isRecording) onEvent(CameraControlsEvent.StopRecording)
                        else onEvent(CameraControlsEvent.StartRecording)
                    }
                )
            }

            // Selector foto / video
            if (allowVideo) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    CameraModeTab(
                        label = "Photo",
                        selected = mode == CameraMode.PHOTO,
                        enabled = !isRecording,
                        onClick = { if (mode != CameraMode.PHOTO) onEvent(CameraControlsEvent.ToggleMode) }
                    )
                    CameraModeTab(
                        label = "Video",
                        selected = mode == CameraMode.VIDEO,
                        enabled = !isRecording,
                        onClick = { if (mode != CameraMode.VIDEO) onEvent(CameraControlsEvent.ToggleMode) }
                    )
                }
            }
        }

    }
}

@Composable
private fun PhotoCaptureButton(
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(Color.Transparent)
            .border(3.dp, Color.White, CircleShape)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable(onClick = onClick)
        )
    }
}

@Composable
private fun VideoCaptureButton(
    isRecording: Boolean,
    onClick: () -> Unit,
) {
    val innerSize by animateDpAsState(
        targetValue = if (isRecording) 28.dp else 56.dp,
        label = "innerSize"
    )

    val cornerRadius by animateDpAsState(
        targetValue = if (isRecording) 6.dp else 28.dp, // 28.dp es la mitad del tamaño para que sea un círculo
        label = "cornerRadius"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .border(3.dp, Color.White, CircleShape)
    ) {
        Box(
            modifier = Modifier
                .size(innerSize)
                .clip(RoundedCornerShape(cornerRadius))
                .background(Color.Red)
                .clickable(onClick = onClick)
        )
    }
}

@Composable
private fun CameraModeTab(
    label: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Text(
        text = label,
        color = if (selected) Color.Yellow else Color.White.copy(alpha = 0.6f),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}