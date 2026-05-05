package app.iesjdlc.tipslab.presentation.screens.lifehack.utils

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import app.iesjdlc.tipslab.domain.model.MediaType

@Composable
fun rememberGalleryLauncher(
    onMediaPicked: (Uri, MediaType) -> Unit
): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {
    val context = LocalContext.current
    return rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val mime = context.contentResolver.getType(it)
            val type = if (mime?.startsWith("video") == true) MediaType.VIDEO else MediaType.IMAGE
            onMediaPicked(it, type)
        }
    }
}