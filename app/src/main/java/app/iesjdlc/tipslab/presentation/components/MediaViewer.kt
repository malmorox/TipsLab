package app.iesjdlc.tipslab.presentation.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import app.iesjdlc.tipslab.domain.model.MediaType
import coil3.compose.AsyncImage
import androidx.core.net.toUri

@Composable
fun MediaViewer(
    mediaUrl: String,
    mediaType: MediaType,
    modifier: Modifier = Modifier
) {
    val mediaUri = remember(mediaUrl) { mediaUrl.toUri() }
    var showFullScreenImageViewer by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f),
        contentAlignment = Alignment.Center
    ) {
        when (mediaType) {
            MediaType.IMAGE -> ImageViewer(
                mediaUri = mediaUri,
                onClick = { showFullScreenImageViewer = true }
            )
            MediaType.VIDEO -> VideoPlayer(
                videoUri = mediaUri
            )
        }
    }

    if (showFullScreenImageViewer) {
        FullScreenImageViewer(
            imageUri = mediaUri,
            onDismiss = { showFullScreenImageViewer = false }
        )
    }
}

@Composable
private fun ImageViewer(
    mediaUri: Uri,
    onClick: () -> Unit
) {
    AsyncImage(
        model = mediaUri,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick() }
    )
}