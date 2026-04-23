package app.iesjdlc.tipslab.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import app.iesjdlc.tipslab.ui.components.CategoryChipsRow

@Composable
fun HomeTab(
    viewModel: HomeViewModel = hiltViewModel(),
    onLifehackClick: (String) -> Unit,
    onOpenCategory: (Int) -> Unit,
    onOpenSearch: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeTabUI(
        state = uiState,
        onLifehackClick = onLifehackClick,
        onOpenCategory = onOpenCategory,
        onSearch = onOpenSearch
    )
}

@Composable
private fun HomeTabUI(
    state: HomeUiState,
    onLifehackClick: (String) -> Unit,
    onOpenCategory: (Int) -> Unit,
    onSearch: () -> Unit
) {

    CategoryChipsRow(
        categories = state.allCategories,
        onCategoryClick = { category ->
            onOpenCategory(category.id)
        }
    )
}

