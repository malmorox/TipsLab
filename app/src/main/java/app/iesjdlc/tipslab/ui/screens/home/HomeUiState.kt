package app.iesjdlc.tipslab.ui.screens.home

import app.iesjdlc.tipslab.domain.model.Category

data class HomeUiState(
    val allCategories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
