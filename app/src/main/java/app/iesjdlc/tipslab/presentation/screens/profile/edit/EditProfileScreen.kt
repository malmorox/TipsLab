package app.iesjdlc.tipslab.presentation.screens.profile.edit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.core.model.CameraMediaResult
import app.iesjdlc.tipslab.presentation.components.ConfirmOrDismissDialog
import app.iesjdlc.tipslab.presentation.components.ProfilePhotoPicker
import coil3.compose.AsyncImage

@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onProfileEdited: () -> Unit,
    onOpenCamera: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.onProfilePhotoPicked(it) }
    }

    EditProfileScreenUI(
        state = uiState,
        onUsernameChange = { viewModel.onUsernameChange(it) },
        onEmailChange = { viewModel.onEmailChange(it) },
        onPasswordChangeClick = {},
        onOpenCamera = onOpenCamera,
        onOpenGallery = {
            galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onPhotoRemove = { viewModel.onPhotoRemove() },
        onSave = { viewModel.onSave(onProfileEdited) },
        onBack = { viewModel.onBackClick(onNavigateBack) },
        onDiscardChangesConfirm = { viewModel.onDiscardChangesConfirm(onNavigateBack) },
        onDiscardChangesDismiss = { viewModel.onDiscardChangesDismiss() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileScreenUI(
    state: EditProfileUiState,
    onEmailChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChangeClick: () -> Unit,
    onOpenCamera: () -> Unit,
    onOpenGallery: () -> Unit,
    onPhotoRemove: () -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onDiscardChangesConfirm: () -> Unit,
    onDiscardChangesDismiss: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.edit_profile),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfilePhotoPicker(
                    photo = state.profilePhoto,
                    onCameraClick = onOpenCamera,
                    onGalleryClick = onOpenGallery,
                    onRemoveClick = onPhotoRemove
                )

                Spacer(modifier = Modifier.height(32.dp))

                FieldSection(label = stringResource(R.string.email)) {

                    OutlinedTextField(
                        value = state.email,
                        onValueChange = onEmailChange,
                        readOnly = true,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                FieldSection(label = stringResource(R.string.username)) {

                    OutlinedTextField(
                        value = state.username,
                        onValueChange = onUsernameChange,
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
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSave,
                enabled = !state.isLoading,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {

                if (state.isLoading) {

                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(22.dp)
                    )

                } else {

                    Text(
                        text = stringResource(R.string.save_changes).uppercase(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            }
        }
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

@Composable
private fun FieldSection(
    label: String,
    content: @Composable () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        content()
    }
}