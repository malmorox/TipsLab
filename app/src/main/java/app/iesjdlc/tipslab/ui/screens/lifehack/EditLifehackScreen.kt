package app.iesjdlc.tipslab.ui.screens.lifehack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun EditLifehackScreen(
    viewModel: EditLifehackViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onLifehackEdited: (String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()

}

@Composable
private fun EditLifehackScreenUI() {

}