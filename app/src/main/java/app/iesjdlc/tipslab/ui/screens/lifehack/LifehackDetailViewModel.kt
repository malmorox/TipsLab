package app.iesjdlc.tipslab.ui.screens.lifehack

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LifehackDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val lifehackRepository: LifehackRepository
) : ViewModel() {
    private val lifehackId = savedStateHandle.toRoute<Route.LifehackDetail>().lifehackId

    private val _uiState = MutableStateFlow(LifehackDetailUiState())
    val uiState: StateFlow<LifehackDetailUiState> = _uiState.asStateFlow()

    init {
        loadLifehack()
    }

    private fun loadLifehack() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                lifehackRepository.getLifehackById(lifehackId)
                    .onSuccess { lifehack ->
                        _uiState.update {
                            it.copy(
                                lifehack = lifehack,
                                isLoading = false
                            )
                        }
                    }
                    .onFailure { error ->
                        _uiState.update { it.copy(error = error.message) }
                    }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onEditClick(
        onNavigate: (String) -> Unit
    ) {
        onNavigate(lifehackId)
    }

    fun onDeleteClick() {
        _uiState.update { it.copy(showConfirmDeleteDialog = true) }
    }

    fun onConfirmDelete(
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                lifehackRepository.deleteLifehack(lifehackId)
                    .onSuccess {
                        onSuccess()
                    }
                    .onFailure { error ->
                        _uiState.update { it.copy(error = error.message) }
                    }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onDismissDelete() {
        _uiState.update { it.copy(showConfirmDeleteDialog = false) }
    }

    fun onCategoryClick(
        onNavigate: (String) -> Unit
    ) {
        uiState.value.lifehack?.category?.let { category ->
            onNavigate(category.id)
        }
    }
}