package app.iesjdlc.tipslab.ui.screens.lifehack

import android.net.Uri
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.MediaType
import app.iesjdlc.tipslab.ui.components.CategorySelector
import app.iesjdlc.tipslab.ui.components.LifehackStepsDialog
import app.iesjdlc.tipslab.ui.components.MediaPicker

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
        onStepsChanged = { viewModel.onStepsChanged(it) },
        onStepsDialogOpen = { viewModel.onStepsDialogOpen() },
        onStepsDialogDismiss = { viewModel.onStepsDialogDismiss() },
        onCategorySelected = { viewModel.onCategoryChanged(it) },
        onCategoryDropdownToggle = { viewModel.onCategoryDropdownToggle() },
        onMediaPicked = { uri, type -> viewModel.onMediaPicked(uri, type) },
        onMediaRemoved = { viewModel.onMediaRemoved() },
        onSubmited = { viewModel.onSubmit(onLifehackCreated) }
    )
}

@Composable
private fun CreateLifehackTabUI(
    state: CreateLifehackUiState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onStepsChanged: (List<String>) -> Unit,
    onStepsDialogOpen: () -> Unit,
    onStepsDialogDismiss: () -> Unit,
    onCategorySelected: (Category) -> Unit,
    onCategoryDropdownToggle: () -> Unit,
    onMediaPicked: (Uri, MediaType) -> Unit,
    onMediaRemoved: () -> Unit,
    onSubmited: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 36.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.create_lifehack),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(24.dp))

            FieldSection(label = stringResource(R.string.media)) {
                MediaPicker(
                    mediaUri = state.mediaLocalUri,
                    mediaType = state.mediaType,
                    onMediaPicked = onMediaPicked,
                    onMediaRemoved = onMediaRemoved
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            FieldSection(label = stringResource(R.string.title)) {
                OutlinedTextField(
                    value = state.title,
                    onValueChange = onTitleChanged,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.title_placeholder),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    },
                    singleLine = true,
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            FieldSection(label = stringResource(R.string.description)) {
                OutlinedTextField(
                    value = state.description,
                    onValueChange = onDescriptionChanged,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.description_placeholder),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            FieldSection(label = stringResource(R.string.steps)) {

                if (state.isStepsDialogOpen) {
                    LifehackStepsDialog(
                        steps = state.steps,
                        onDismiss = onStepsDialogDismiss,
                        onConfirm = { steps ->
                            onStepsChanged(steps)
                            onStepsDialogDismiss()

                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            FieldSection(label = stringResource(R.string.category)) {
                CategorySelector(
                    categories = state.availableCategories,
                    selectedCategory = state.category,
                    expanded = state.isCategoryDropdownExpanded,
                    onToggle = onCategoryDropdownToggle,
                    onCategorySelected = onCategorySelected
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSubmited,
                enabled = !state.isLoading,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = stringResource(R.string.publish).uppercase(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun FieldSection(
    label: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        content()
    }
}