package app.iesjdlc.tipslab.ui.screens.lifehack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LifehackDetailScreen(
    viewModel: LifehackDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onEditLifehack: () -> Unit,
    onDeleteLifehack: () -> Unit,
    onOpenCategory: (String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()

    LifehackDetailScreenUI(
        state = uiState.value,
        onEdit = onEditLifehack,
        onDelete = onDeleteLifehack,
        onOpenCategory = onOpenCategory,
        onBack = onNavigateBack
    )
}

@Composable
private fun LifehackDetailScreenUI(
    state: LifehackDetailUiState,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onOpenCategory: (String) -> Unit,
    onBack: () -> Unit,
) {

}