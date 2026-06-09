package app.iesjdlc.tipslab.presentation.screens.category

import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.presentation.common.SectionState

data class CategoryUiState(
    val category: Category? = null,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val sections: ContentSections = ContentSections(),
    val search: SearchState = SearchState()
) {
    val globalContentError: Boolean
        get() = sections.recent.error != null && sections.popular.error != null

    val globalContentNoData: Boolean
        get() = !sections.popular.isLoading && !sections.recent.isLoading
                && sections.popular.data.isNullOrEmpty() && sections.recent.data.isNullOrEmpty()
}

data class ContentSections(
    val recent: SectionState<List<Lifehack>> = SectionState(),
    val popular: SectionState<List<Lifehack>> = SectionState(),
)

data class SearchState(
    val query: String = "",
    val isActive: Boolean = false,
    val isLoading: Boolean = false,
    val results: List<Lifehack> = emptyList()
)
