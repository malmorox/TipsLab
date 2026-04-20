package app.iesjdlc.tipslab.ui.screens.lifehack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.ui.components.ConfirmOrDismissDialog

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
        onDelete = { viewModel.onDeleteClick() },
        onConfirmDelete = { viewModel.onConfirmDelete(onDeleteLifehack) },
        onDismissDelete = { viewModel.onDismissDelete() },
        onOpenCategory = onOpenCategory,
        onBack = onNavigateBack
    )
}

@Composable
private fun LifehackDetailScreenUI(
    state: LifehackDetailUiState,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onConfirmDelete: () -> Unit,
    onDismissDelete: () -> Unit,
    onOpenCategory: (String) -> Unit,
    onBack: () -> Unit,
) {
    if (state.showConfirmDeleteDialog) {
        ConfirmOrDismissDialog(
            onConfirm = onConfirmDelete,
            onDismiss = onDismissDelete,
            title = stringResource(R.string.delete_lifehack),
            message = stringResource(R.string.delete_lifehack_message),
            confirmText = stringResource(R.string.delete),
            dismissText = stringResource(R.string.cancel)
        )
    }
}