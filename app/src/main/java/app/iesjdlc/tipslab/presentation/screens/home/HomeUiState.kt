package app.iesjdlc.tipslab.presentation.screens.home

import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.Lifehack

data class HomeUiState(
    val content: HomeContent = HomeContent(),
    val isLoading: Boolean = false,

    // Errores de carga
    val forYouLifehacksError: String? = null,
    val trendingLifehacksError: String? = null,
    val popularCategoriesError: String? = null,
    val recentLifehacksError: String? = null
)

data class HomeContent(
    val forYouLifehacks: List<Lifehack> = emptyList(),
    val trendingLifehacks: List<Lifehack> = emptyList(),
    val popularCategories: List<Category> = emptyList(),
    val recentLifehacks: List<Lifehack> = emptyList()
)
