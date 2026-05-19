package app.iesjdlc.tipslab.presentation.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.repository.SearchRepository
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
    private val saveSearchUseCase: SaveSearchUseCase
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
                    _uiState.update { it.copy(suggestions = history) }
                }
                .onFailure {
                    //TODO manejar error
                }
        }
    }

    fun onQueryChange(newValue: String) {
        _uiState.update { it.copy(query = newValue) }
    }

    fun onSuggestionClick(suggestion: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    query = suggestion,
                    showSuggestions = false,
                    suggestions = emptyList()
                )
            }
            saveSearchUseCase(suggestion)
            search(suggestion)
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