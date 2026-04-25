package app.iesjdlc.tipslab.ui.screens.explore

import app.iesjdlc.tipslab.domain.model.Category

data class ExploreUiState(
    val allCategories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
