package app.iesjdlc.tipslab.presentation.screens.explore

import app.iesjdlc.tipslab.domain.model.Lifehack

enum class SearchPhase {
    IDLE, // Sin query: muestra historial
    SUGGESTING,  // query >= 2: muestra sugerencias (o historial si no hay)
    RESULTS // Después de buscar: muestra resultados
}

data class SearchUiState(
    val query: String = "",
    val searchHistory: List<String> = emptyList(),
    val suggestions: List<String> = emptyList(),
    val results: List<Lifehack> = emptyList(),
    val isLoading: Boolean = false,
    val phase: SearchPhase = SearchPhase.IDLE,
    val errorMessage: String? = null
)