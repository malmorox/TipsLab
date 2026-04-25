package app.iesjdlc.tipslab.ui.screens.profile.edit

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

}

@Composable
private fun EditProfileScreenUI() {

}