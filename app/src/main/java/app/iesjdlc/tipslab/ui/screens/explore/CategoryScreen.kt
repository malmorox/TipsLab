package app.iesjdlc.tipslab.ui.screens.explore

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LifehacksByCategoryScreen(
    viewModel: CategoryViewModel = hiltViewModel(),
    categoryId: String,
    onNavigateBack: () -> Unit,
    onLifehackClick: (String) -> Unit
) {

}

@Composable
private fun LifehacksByCategoryScreenUI() {

}