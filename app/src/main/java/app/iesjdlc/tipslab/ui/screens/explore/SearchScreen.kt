package app.iesjdlc.tipslab.ui.screens.explore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    SearchScreenUI(
        state = uiState
    )
}

@Composable
private fun SearchScreenUI(
    state: SearchUiState
) {

}