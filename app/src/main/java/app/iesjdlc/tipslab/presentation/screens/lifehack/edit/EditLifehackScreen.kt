package app.iesjdlc.tipslab.presentation.screens.lifehack.edit

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EditLifehackScreen(
    viewModel: EditLifehackViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onLifehackEdited: (String) -> Unit,
    onOpenCamera: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    EditLifehackScreenUI(
        state = uiState.value,
        onOpenCamera = onOpenCamera,
        onSave = { viewModel.onSave(onLifehackEdited) },
        onBack = onNavigateBack
    )
}

@Composable
private fun EditLifehackScreenUI(
    state: EditLifehackUiState,
    onOpenCamera: () -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {

}