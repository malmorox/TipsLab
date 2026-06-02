package app.iesjdlc.tipslab.presentation.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.repository.SearchRepository
import app.iesjdlc.tipslab.domain.usecase.search.ClearSearchHistoryUseCase
import app.iesjdlc.tipslab.domain.usecase.search.GetSearchHistoryUseCase
import app.iesjdlc.tipslab.domain.usecase.search.SaveSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val saveSearchUseCase: SaveSearchUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var suggestionsJob: Job? = null

    init {
        loadSearchHistory()
    }

    private fun loadSearchHistory() {
        viewModelScope.launch {
           getSearchHistoryUseCase()
                .onSuccess { history ->
                    _uiState.update { it.copy(searchHistory = history) }
                }
                .onFailure {
                    //TODO manejar error
                }
        }
    }

    fun onQueryChange(newValue: String) {
        suggestionsJob?.cancel()
        _uiState.update { it.copy(query = newValue) }

        when {
            newValue.isBlank() -> {
                _uiState.update {
                    it.copy(
                        phase = SearchPhase.IDLE,
                        suggestions = emptyList(),
                        results = emptyList()
                    )
                }
            }
            newValue.length >= 2 -> {
                suggestionsJob = viewModelScope.launch {
                    delay(300)
                    loadSuggestions(newValue)
                }
            }
        }
    }

    fun onSuggestionClick(suggestion: String) {
        _uiState.update { it.copy(query = suggestion) }
        triggerSearch(suggestion)
    }

    fun onSearchSubmit(query: String = _uiState.value.query) {
        if (query.isBlank()) return
        _uiState.update { it.copy(query = query) }
        triggerSearch(query)
    }

    fun onClearHistory() {
        viewModelScope.launch {
            clearSearchHistoryUseCase()
            _uiState.update { it.copy(searchHistory = emptyList()) }
        }
    }

    fun onHistoryItemClick(item: String) {
        _uiState.update { it.copy(query = item) }
        triggerSearch(item)
    }

    fun onLifehackClick(
        lifehack: Lifehack,
        onNavigate: (String) -> Unit
    ) {
        onNavigate(lifehack.id)
    }

    private fun triggerSearch(query: String) {
        suggestionsJob?.cancel()
        viewModelScope.launch {
            saveSearchUseCase(query)
            loadSearchHistory()
            search(query)
        }
    }

    private suspend fun loadSuggestions(query: String) {
        searchRepository.getSearchSuggestions(query)
            .onSuccess { suggestions ->
                _uiState.update {
                    it.copy(
                        suggestions = suggestions,
                        isLoading = false,
                        phase = if (suggestions.isNotEmpty()) SearchPhase.SUGGESTING else SearchPhase.IDLE
                    )
                }
            }
    }

    private suspend fun search(query: String) {
        _uiState.update { it.copy(isLoading = true, phase = SearchPhase.RESULTS) }
        searchRepository.searchLifehacks(query)
            .onSuccess { results ->
                _uiState.update {
                    it.copy(
                        results = results,
                        isLoading = false
                    )
                }
            }
            .onFailure { error ->
                _uiState.update {
                    it.copy(
                        errorMessage = error.message,
                        isLoading = false
                    )
                }
            }
    }
}