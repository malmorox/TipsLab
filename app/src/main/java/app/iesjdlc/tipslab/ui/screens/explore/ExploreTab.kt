package app.iesjdlc.tipslab.ui.screens.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.ui.components.CategoryCard
import app.iesjdlc.tipslab.ui.components.SearchBar

@Composable
fun ExploreTab(
    viewModel: ExploreViewModel = hiltViewModel(),
    onOpenCategory: (Int) -> Unit,
    onSearch: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ExploreTabUI(
        state = uiState,
        onOpenCategory = { category -> viewModel.onCategoryClick(category, onOpenCategory) },
        onSearch = onSearch
    )
}

@Composable
private fun ExploreTabUI(
    state: ExploreUiState,
    onOpenCategory: (Category) -> Unit,
    onSearch: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp, 24.dp, 24.dp, 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SearchBar(
                readOnly = true,
                onClick  = onSearch
            )

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cargando...",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.allCategories) { category ->
                        CategoryCard(
                            category = category,
                            onClick = { onOpenCategory(category) }
                        )
                    }
                }
            }
        }
    }
}