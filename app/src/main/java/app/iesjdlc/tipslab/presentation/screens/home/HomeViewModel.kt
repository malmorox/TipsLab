package app.iesjdlc.tipslab.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.model.screen_sections.CategorySection
import app.iesjdlc.tipslab.domain.model.screen_sections.HomeSection
import app.iesjdlc.tipslab.domain.repository.CategoryRepository
import app.iesjdlc.tipslab.domain.usecase.lifehack.GetHomeLifehacksUseCase
import app.iesjdlc.tipslab.presentation.common.SectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeLifehacksUseCase: GetHomeLifehacksUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun refresh() = loadData(isRefresh = true)

    private fun loadData(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) _uiState.update { it.copy(isRefreshing = true) }

            HomeSection.entries
                .map { section -> launch { loadSection(section) } }
                .joinAll()

            if (isRefresh) _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    private fun loadSection(section: HomeSection, limit: Int = 10) {
        viewModelScope.launch {
            updateSection(section, SectionState(isLoading = true))

            getHomeLifehacksUseCase(section, limit)
                .onSuccess { result ->
                    updateSection(section, SectionState(data = result))
                }
                .onFailure { error ->
                    updateSection(section, SectionState(error = error.message))
                }
        }
    }

    private fun updateSection(section: HomeSection, newState: SectionState<List<Lifehack>>) {
        _uiState.update { state ->
            when (section) {
                HomeSection.RECENT -> state.copy(
                    sections = state.sections.copy(recent = newState)
                )
                HomeSection.TRENDING -> state.copy(
                    sections = state.sections.copy(trending = newState)
                )
                HomeSection.FOR_YOU -> state.copy(
                    sections = state.sections.copy(forYou = newState)
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
}