package app.iesjdlc.tipslab.ui.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val categories = categoryRepository.getAllCategories()
                    .getOrElse { emptyList() }
                _uiState.update { it.copy(allCategories = categories) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onCategoryClick(
        category: Category,
        onNavigate: (Int) -> Unit
    ) {
        onNavigate(category.id)
    }
}