package app.iesjdlc.tipslab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.domain.model.MediaSource
import app.iesjdlc.tipslab.domain.model.MediaType
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.video.VideoFrameDecoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaPicker(
    mediaSource: MediaSource?,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onMediaRemoved: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSheet by remember { mutableStateOf(false) }

    if (mediaSource == null) {
        MediaEmpty(
            onClick = { showSheet = true },
            modifier = modifier
        )
    } else {
        MediaPreview(
            source = mediaSource,
            onRemove = onMediaRemoved,
            onReplace = { showSheet = true },
            modifier = modifier
        )
    }

    if (showSheet) {
        MediaPickerSheet(
            onOpenCamera = {
                showSheet = false
                onCameraClick()
            },
            onPickFromGallery = {
                showSheet = false
                onGalleryClick()
            },
            onDismiss = { showSheet = false }
        )
    }
}

@Composable
private fun MediaEmpty(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
            color = Color.Transparent,
            modifier = Modifier
                .wrapContentWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Subir foto o vídeo",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun MediaPreview(
    source: MediaSource,
    onRemove: () -> Unit,
    onReplace: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Extraemos el thumbnail del primer frame
    val model = remember(source) {
        when (source) {
            is MediaSource.Local -> {
                if (source.type == MediaType.VIDEO) {
                    ImageRequest.Builder(context)
                        .data(source.uri)
                        .decoderFactory { result, options, _ ->
                            VideoFrameDecoder(result.source, options)
                        }
                        .build()
                } else {
                    source.uri
                }
            }
            is MediaSource.Remote -> {
                source.url
            }
        }
    }

    val isVideo = source.mediaType == MediaType.VIDEO

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onReplace() }
    ) {
        AsyncImage(
            model = model,
            contentDescription = "Preview",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        if (isVideo) {
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.65f),
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .zIndex(1f)
                .size(28.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.35f))
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = stringResource(R.string.remove_media),
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}