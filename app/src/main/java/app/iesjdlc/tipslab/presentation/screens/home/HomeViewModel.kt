package app.iesjdlc.tipslab.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {


            try {
                val categoriesResult = categoryRepository.getAllCategories()

                categoriesResult.onSuccess { categories ->
                    _uiState.update { it.copy(allCategories = categories) }
                }
            } finally {

            }
        }
    }
}