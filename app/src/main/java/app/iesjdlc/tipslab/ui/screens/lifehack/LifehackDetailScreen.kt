package app.iesjdlc.tipslab.ui.screens.lifehack

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.More
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.ui.components.ConfirmOrDismissDialog

@Composable
fun LifehackDetailScreen(
    viewModel: LifehackDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onEditLifehack: (String) -> Unit,
    onDeleteLifehack: () -> Unit,
    onOpenCategory: (String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()

    LifehackDetailScreenUI(
        state = uiState.value,
        onEdit = { viewModel.onEditClick(onEditLifehack) },
        onDelete = { viewModel.onDeleteClick() },
        onConfirmDelete = { viewModel.onConfirmDelete(onDeleteLifehack) },
        onDismissDelete = { viewModel.onDismissDelete() },
        onOpenCategory = { viewModel.onCategoryClick(onOpenCategory) },
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
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.go_back),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = { /* TODO abrir menu */ },
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.AutoMirrored.Rounded.More,
                    contentDescription = stringResource(R.string.go_back),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = state.lifehack?.title.orEmpty(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = state.lifehack?.description.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }

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