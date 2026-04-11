package app.iesjdlc.tipslab.ui.screens.lifehack

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.MediaType

@Composable
fun CreateLifehackTab(
    viewModel: CreateLifehackViewModel = hiltViewModel(),
    onLifehackCreated: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    CreateLifehackTabUI(
        state = uiState,
        onTitleChanged = { viewModel.onTitleChanged(it) },
        onDescriptionChanged = { viewModel.onDescriptionChanged(it) },
        onCategorySelected = { viewModel.onCategoryChanged(it) },
        onCategoryDropdownToggle = { viewModel.onCategoryDropdownToggle() },
        onSubmited = { viewModel.onSubmit(onLifehackCreated) }
    )
}

@Composable
private fun CreateLifehackTabUI(
    state: CreateLifehackUiState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCategorySelected: (Category) -> Unit,
    onCategoryDropdownToggle: () -> Unit,
    onSubmited: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text("Pestaña de creación de lifehack")
    }
}

@Composable
private fun FieldSection(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        content()
    }
}