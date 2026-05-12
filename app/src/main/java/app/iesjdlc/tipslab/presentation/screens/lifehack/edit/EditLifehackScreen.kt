package app.iesjdlc.tipslab.presentation.screens.lifehack.edit

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.core.constants.FormConstants
import app.iesjdlc.tipslab.core.model.CameraMediaResult
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.presentation.components.CategorySelectorSheet
import app.iesjdlc.tipslab.presentation.components.ConfirmOrDismissDialog
import app.iesjdlc.tipslab.presentation.components.FormFieldSection
import app.iesjdlc.tipslab.presentation.components.LifehackStepsSheet
import app.iesjdlc.tipslab.presentation.common.rememberLifehackGalleryLauncher
import app.iesjdlc.tipslab.presentation.components.LifehackMediaPicker

@Composable
fun EditLifehackScreen(
    viewModel: EditLifehackViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onLifehackEdited: (String) -> Unit,
    onOpenCamera: () -> Unit,
    cameraResult: CameraMediaResult? = null,
    onCameraResultConsumed: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val galleryLauncher = rememberLifehackGalleryLauncher { uri, type ->
        viewModel.onMediaPicked(uri, type)
    }

    LaunchedEffect(cameraResult) {
        cameraResult?.let {
            viewModel.onCameraResult(it)
            onCameraResultConsumed()
        }
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

@OptIn(ExperimentalMaterial3Api::class)
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.edit),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.go_back),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LifehackMediaPicker(
                        mediaSource = state.mediaSource,
                        onCameraClick = onOpenCamera,
                        onGalleryClick = onOpenGallery,
                        onMediaRemoved = onMediaRemove
                    )

                    FormFieldSection(
                        label = stringResource(R.string.title),
                        errorMessage = state.titleErrorMessage?.asString()
                    ) {
                        OutlinedTextField(
                            value = state.title,
                            onValueChange = onTitleChange,
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
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                    alpha = 0.7f
                                ),
                                cursorColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }

                    FormFieldSection(
                        label = stringResource(R.string.description),
                        errorMessage = state.descriptionErrorMessage?.asString()
                    ) {
                        Box {
                            OutlinedTextField(
                                value = state.description,
                                onValueChange = onDescriptionChange,
                                placeholder = {
                                    Text(
                                        text = stringResource(R.string.description_placeholder),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.4f
                                        )
                                    )
                                },
                                enabled = !state.isLoading,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                shape = MaterialTheme.shapes.medium,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                        alpha = 0.7f
                                    ),
                                    cursorColor = MaterialTheme.colorScheme.primary
                                )
                            )

                            Text(
                                text = "${state.description.length}/${FormConstants.MAX_DESCRIPTION_LENGTH}",
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(end = 12.dp, bottom = 8.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                    }

                    FormFieldSection(label = stringResource(R.string.steps)) {
                        Surface(
                            onClick = onStepsSheetOpen,
                            enabled = !state.isLoading,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    shape = MaterialTheme.shapes.medium
                                ),
                            color = Color.Transparent
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (state.stepsCount > 0) {
                                    Text(
                                        text = "${state.stepsCount} ${
                                            if (state.stepsCount == 1) stringResource(
                                                R.string.step
                                            ) else stringResource(R.string.steps)
                                        }",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                } else {
                                    Text(
                                        text = stringResource(R.string.steps_placeholder),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.4f
                                        )
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Rounded.ChevronRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }

                    FormFieldSection(
                        label = stringResource(R.string.category),
                        errorMessage = state.categoryErrorMessage?.asString()
                    ) {
                        Surface(
                            onClick = onCategorySheetOpen,
                            enabled = !state.isLoading,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    shape = MaterialTheme.shapes.medium
                                ),
                            color = Color.Transparent
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (state.category != null) {
                                    Text(
                                        text = state.category.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                } else {
                                    Text(
                                        text = stringResource(R.string.category_placeholder),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.4f
                                        )
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Rounded.ChevronRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = onSave,
                    enabled = !state.isLoading,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = stringResource(R.string.save).uppercase(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                    )
                }
            }
        }
    }

    if (state.showStepsSheet) {
        LifehackStepsSheet(
            steps = state.steps,
            onConfirm = { steps ->
                onStepsChange(steps)
                onStepsSheetDismiss()
            },
            onDismiss = onStepsSheetDismiss
        )
    }

    if (state.showCategorySheet) {
        CategorySelectorSheet(
            categories = state.allCategories,
            selectedCategory = state.category,
            onCategorySelected = {
                onCategoryChange(it)
                onCategorySheetDismiss()
            },
            onDismiss = onCategorySheetDismiss
        )
    }

    if (state.showDiscardChangesDialog) {
        ConfirmOrDismissDialog(
            onConfirm = onDiscardChangesConfirm,
            onDismiss = onDiscardChangesDismiss,
            title = stringResource(R.string.discard_changes),
            message = stringResource(R.string.discard_changes_message),
            confirmText = stringResource(R.string.discard),
            dismissText = stringResource(R.string.cancel)
        )
    }
}