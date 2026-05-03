package app.iesjdlc.tipslab.presentation.screens.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import app.iesjdlc.tipslab.domain.model.CategorySection
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.repository.CategoryRepository
import app.iesjdlc.tipslab.domain.usecase.lifehack.GetLifehacksByCategoryUseCase
import app.iesjdlc.tipslab.domain.usecase.lifehack.SearchLifehacksByCategoryUseCase
import app.iesjdlc.tipslab.presentation.common.SectionState
import app.iesjdlc.tipslab.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository,
    private val getLifehacksByCategoryUseCase: GetLifehacksByCategoryUseCase,
    private val searchLifehacksByCategoryUseCase: SearchLifehacksByCategoryUseCase
) : ViewModel() {
    private val categoryId = savedStateHandle.toRoute<Route.LifehacksByCategory>().categoryId

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        loadCategory()
        loadCategoryLifehacks()
    }

    private fun loadCategory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                categoryRepository.getCategoryById(categoryId)
                    .onSuccess { category ->
                        _uiState.update { it.copy(category = category) }
                    }
                    .onFailure { error ->
                        _uiState.update { it.copy(errorMessage = error.message) }
                    }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadCategoryLifehacks() {
        loadSection(CategorySection.RECENT)
        loadSection(CategorySection.POPULAR)
    }

    private fun loadSection(section: CategorySection, limit: Int = 10) {
        viewModelScope.launch {
            updateSection(section, SectionState(isLoading = true))

            getLifehacksByCategoryUseCase(categoryId, section, limit = limit)
                .onSuccess { result ->
                    updateSection(section, SectionState(data = result))
                }
                .onFailure { error ->
                    updateSection(section, SectionState(error = error.message))
                }
        }
    }

    private fun updateSection(section: CategorySection, newState: SectionState<List<Lifehack>>) {
        _uiState.update { state ->
            when (section) {
                CategorySection.RECENT -> state.copy(
                    sections = state.sections.copy(recent = newState)
                )
                CategorySection.POPULAR -> state.copy(
                    sections = state.sections.copy(popular = newState)
                )
            }
        }
    }

    fun onLifehackClick(
        lifehack: Lifehack,
        onNavigate: (String) -> Unit
    ) {
        onNavigate(lifehack.id)
    }

    fun onSearchQueryChange(newValue: String) {
        _uiState.update { it.copy(search = it.search.copy(query = newValue)) }


    }
}