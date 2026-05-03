package app.iesjdlc.tipslab.presentation.screens.camera

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import app.iesjdlc.tipslab.domain.model.MediaType

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel(),
    onMediaCaptured: (Uri, MediaType) -> Unit,
    onNavigateBack: () -> Unit,
) {

}