package app.iesjdlc.tipslab.presentation.screens.lifehack.edit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.MediaType
import app.iesjdlc.tipslab.presentation.screens.lifehack.utils.rememberGalleryLauncher

@Composable
fun EditLifehackScreen(
    viewModel: EditLifehackViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onLifehackEdited: (String) -> Unit,
    onOpenCamera: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val galleryLauncher = rememberGalleryLauncher { uri, type ->
        viewModel.onMediaPicked(uri, type)
    }

    EditLifehackScreenUI(
        state = uiState.value,
        onTitleChange = { viewModel.onTitleChange(it) },
        onDescriptionChange = { viewModel.onDescriptionChange(it) },
        onStepsChange = { viewModel.onStepsChange(it) },
        onStepsSheetOpen = { viewModel.onStepsSheetOpen() },
        onStepsSheetDismiss = { viewModel.onStepsSheetDismiss() },
        onCategoryChange = { viewModel.onCategoryChange(it) },
        onCategorySheetOpen = { viewModel.onCategorySheetOpen() },
        onCategorySheetDismiss = { viewModel.onCategorySheetDismiss() },
        onOpenCamera = onOpenCamera,
        onOpenGallery = {
            galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
            )
        },
        onMediaRemove = { viewModel.onMediaRemove() },
        onSave = { viewModel.onSave(onLifehackEdited) },
        onBack = { viewModel.onBackClick(onNavigateBack) },
        onDiscardChangesConfirm = { viewModel.onDiscardChangesConfirm(onNavigateBack) },
        onDiscardChangesDismiss = { viewModel.onDiscardChangesDismiss() }
    )
}

@Composable
private fun EditLifehackScreenUI(
    state: EditLifehackUiState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onStepsChange: (List<String>) -> Unit,
    onStepsSheetOpen: () -> Unit,
    onStepsSheetDismiss: () -> Unit,
    onCategoryChange: (Category) -> Unit,
    onCategorySheetOpen: () -> Unit,
    onCategorySheetDismiss: () -> Unit,
    onOpenCamera: () -> Unit,
    onOpenGallery: () -> Unit,
    onMediaRemove: () -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onDiscardChangesConfirm: () -> Unit,
    onDiscardChangesDismiss: () -> Unit,
) {

}