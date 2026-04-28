package app.iesjdlc.tipslab.presentation.screens.category

import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.Lifehack

data class CategoryUiState(
    val category: Category? = null,
    val categoryLifehacks: CategoryLifehacks = CategoryLifehacks(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class CategoryLifehacks(
    val recent: List<Lifehack> = emptyList(),
    val popular: List<Lifehack> = emptyList()
)
