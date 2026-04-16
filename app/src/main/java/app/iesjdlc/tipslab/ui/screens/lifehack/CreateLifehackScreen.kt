package app.iesjdlc.tipslab.ui.screens.lifehack

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import app.iesjdlc.tipslab.core.constants.AppConstants.MAX_DESCRIPTION_LENGTH
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.MediaType
import app.iesjdlc.tipslab.ui.components.CategorySelectorSheet
import app.iesjdlc.tipslab.ui.components.FormFieldSection
import app.iesjdlc.tipslab.ui.components.LifehackStepsSheet
import app.iesjdlc.tipslab.ui.components.MediaPicker

@Composable
fun CreateLifehackScreen(
    viewModel: CreateLifehackViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onLifehackCreated: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    CreateLifehackScreenUI(
        state = uiState,
        onTitleChanged = { viewModel.onTitleChanged(it) },
        onDescriptionChanged = { viewModel.onDescriptionChanged(it) },
        onStepsChanged = { viewModel.onStepsChanged(it) },
        onStepsSheetOpen = { viewModel.onStepsSheetOpen() },
        onStepsSheetDismiss = { viewModel.onStepsSheetDismiss() },
        onCategorySelected = { viewModel.onCategoryChanged(it) },
        onCategorySheetOpen = { viewModel.onCategorySheetOpen() },
        onCategorySheetDismiss = { viewModel.onCategorySheetDismiss() },
        onMediaPicked = { uri, type -> viewModel.onMediaPicked(uri, type) },
        onMediaRemoved = { viewModel.onMediaRemoved() },
        onSubmited = { viewModel.onSubmit(onLifehackCreated) },
        onClose = onNavigateBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateLifehackScreenUI(
    state: CreateLifehackUiState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onStepsChanged: (List<String>) -> Unit,
    onStepsSheetOpen: () -> Unit,
    onStepsSheetDismiss: () -> Unit,
    onCategorySelected: (Category) -> Unit,
    onCategorySheetOpen: () -> Unit,
    onCategorySheetDismiss: () -> Unit,
    onMediaPicked: (Uri, MediaType) -> Unit,
    onMediaRemoved: () -> Unit,
    onSubmited: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 36.dp),
    )   {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.create_lifehack),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Start
                    )

                    IconButton(
                        onClick = { onClose() }
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.close),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                FormFieldSection(label = stringResource(R.string.media)) {
                    MediaPicker(
                        mediaUri = state.mediaLocalUri,
                        mediaType = state.mediaType,
                        onMediaPicked = onMediaPicked,
                        onMediaRemoved = onMediaRemoved
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                FormFieldSection(
                    label = stringResource(R.string.title),
                    errorMessage = state.titleErrorMessage
                ) {
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

                FormFieldSection(
                    label = stringResource(R.string.description),
                    errorMessage = state.descriptionErrorMessage
                ) {
                    Box {
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
                            text = "${state.description.length}/$MAX_DESCRIPTION_LENGTH",
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 12.dp, bottom = 8.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                                    text = "${state.stepsCount} ${if (state.stepsCount == 1) stringResource(R.string.step) else stringResource(R.string.steps)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Text(
                                    text = stringResource(R.string.steps_placeholder),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
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

                Spacer(modifier = Modifier.height(16.dp))

                FormFieldSection(
                    label = stringResource(R.string.category),
                    errorMessage = state.categoryErrorMessage
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
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
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
        }

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

    if (state.isStepsSheetOpen) {
        LifehackStepsSheet(
            steps = state.steps,
            onConfirm = { steps ->
                onStepsChanged(steps)
                onStepsSheetDismiss()
            },
            onDismiss = onStepsSheetDismiss
        )
    }

    if (state.isCategorySheetOpen) {
        CategorySelectorSheet(
            categories = state.availableCategories,
            selectedCategory = state.category,
            onCategorySelected = {
                onCategorySelected(it)
                onCategorySheetDismiss()
            },
            onDismiss = onCategorySheetDismiss
        )
    }
}