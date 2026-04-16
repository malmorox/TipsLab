package app.iesjdlc.tipslab.ui.screens.lifehack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LifehackDetailScreen(
    viewModel: LifehackDetailViewModel = hiltViewModel(),
    lifehackId: String,
    onNavigateBack: () -> Unit,
    onEditLifehack: () -> Unit,
    onDeleteLifehack: () -> Unit,
    onOpenCategory: (String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()

}

@Composable
private fun LifehackDetailScreenUI() {

}