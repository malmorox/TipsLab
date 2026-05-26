package app.iesjdlc.tipslab.presentation.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.repository.SearchRepository
import app.iesjdlc.tipslab.domain.usecase.search.ClearSearchHistoryUseCase
import app.iesjdlc.tipslab.domain.usecase.search.GetSearchHistoryUseCase
import app.iesjdlc.tipslab.domain.usecase.search.SaveSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
        _uiState.update { it.copy(query = newValue) }

        if (newValue.isBlank()) {
            _uiState.update {
                it.copy(
                    showSuggestions = false,
                    showResults = false,
                    suggestions = emptyList(),
                    results = emptyList()
                )
            }
        } else {
            viewModelScope.launch { loadSuggestions(newValue) }
        }
    }

    fun onSearch(query: String) {
        _uiState.update { it.copy(query = query) }
        viewModelScope.launch {
            saveSearchUseCase(query)
            loadSearchHistory()
            _uiState.update { it.copy(showSuggestions = false, suggestions = emptyList()) }
            search(query)
        }
    }

    fun onClearHistory() {
        viewModelScope.launch {
            clearSearchHistoryUseCase()
            _uiState.update { it.copy(suggestions = emptyList()) }
        }
    }

    private suspend fun loadSuggestions(query: String) {
        searchRepository.getSearchSuggestions(query)
            .onSuccess { suggestions ->
                _uiState.update {
                    it.copy(
                        suggestions = suggestions,
                        showSuggestions = suggestions.isNotEmpty(),
                        showResults = false
                    )
                }
            }
    }

    private suspend fun search(query: String) {
        _uiState.update { it.copy(isLoading = true, showResults = true) }
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