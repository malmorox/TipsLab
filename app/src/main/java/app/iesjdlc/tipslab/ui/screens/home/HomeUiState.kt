package app.iesjdlc.tipslab.ui.screens.home

import app.iesjdlc.tipslab.domain.model.Category

data class HomeUiState(
    val allCategories: List<Category> = emptyList(),

    // Estados de carga
    val isLoadingLifehacks: Boolean = false,
    val isLoadingCategories: Boolean = false,

    // Errores de carga

)
