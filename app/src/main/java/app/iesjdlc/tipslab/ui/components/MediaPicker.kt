package app.iesjdlc.tipslab.ui.components

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.iesjdlc.tipslab.domain.model.MediaType

@Composable
fun MediaPicker(
    mediaUri: Uri?,
    onMediaPicked: (Uri) -> Unit,
    onMediaRemoved: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                MaterialTheme.shapes.medium
            )
    ) {

    }
}

@Composable
private fun MediaEmpty(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

}

@Composable
private fun MediaPreview(
    uri: Uri,
    type: MediaType?,
    onRemove: () -> Unit,
    onReplace: () -> Unit,
    modifier: Modifier = Modifier
) {

}