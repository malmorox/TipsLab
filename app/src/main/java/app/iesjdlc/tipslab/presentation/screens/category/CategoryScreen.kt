package app.iesjdlc.tipslab.presentation.screens.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onOpenLifehack: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CategoryScreenUI(
        state = uiState,
        onBack = onNavigateBack,
    )
}

@Composable
private fun CategoryScreenUI(
    state: CategoryUiState,
    onBack: () -> Unit
) {

}