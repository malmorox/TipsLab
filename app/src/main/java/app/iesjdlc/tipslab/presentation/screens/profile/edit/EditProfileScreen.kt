package app.iesjdlc.tipslab.presentation.screens.profile.edit

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onProfileEdited: () -> Unit,
    onOpenCamera: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

}

@Composable
private fun EditProfileScreenUI() {

}