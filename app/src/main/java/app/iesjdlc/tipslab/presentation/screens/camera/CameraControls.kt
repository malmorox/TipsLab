package app.iesjdlc.tipslab.presentation.screens.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CameraControls(
    allowVideo: Boolean,
    isRecording: Boolean,
    onEvent: (CameraControlsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {

        // Botón cerrar
        IconButton(
            onClick = { onEvent(CameraControlsEvent.NavigateBack) },
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Cerrar",
                tint = Color.White
            )
        }

        // Botón de captura
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .size(72.dp)
                .clip(CircleShape)
                .background(if (isRecording) Color.Red else Color.White)
                .clickable {
                    when {
                        isRecording -> onEvent(CameraControlsEvent.StopRecording)
                        allowVideo -> onEvent(CameraControlsEvent.StartRecording)
                        else -> onEvent(CameraControlsEvent.CapturePhoto)
                    }
                }
        ) {}
    }
}