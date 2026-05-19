package app.iesjdlc.tipslab.presentation.screens.explore

import app.iesjdlc.tipslab.domain.model.Lifehack

data class SearchUiState(
    val query: String = "",
    val searchHistory: List<String> = emptyList(),
    val suggestions: List<String> = emptyList(),
    val results: List<Lifehack> = emptyList(),
    val isLoading: Boolean = false,
    val showSuggestions: Boolean = false,
    val showResults: Boolean = false,
    val errorMessage: String? = null
)