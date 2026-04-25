package app.iesjdlc.tipslab.ui.screens.lifehack.edit

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EditLifehackScreen(
    viewModel: EditLifehackViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onLifehackEdited: (String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

}

@Composable
private fun EditLifehackScreenUI(
    state: EditLifehackUiState,
    onEdit: () -> Unit,
    onBack: () -> Unit
) {

}