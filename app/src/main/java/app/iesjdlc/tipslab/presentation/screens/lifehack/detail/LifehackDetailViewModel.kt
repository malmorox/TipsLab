package app.iesjdlc.tipslab.presentation.screens.lifehack.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.usecase.lifehack.GetLifehackDetailUseCase
import app.iesjdlc.tipslab.presentation.navigation.Route
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
    private val getLifehackDetailUseCase: GetLifehackDetailUseCase,
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
                getLifehackDetailUseCase(lifehackId)
                    .onSuccess { lifehackDetail ->
                        _uiState.update {
                            it.copy(
                                lifehack = lifehackDetail.lifehack,
                                isAuthor = lifehackDetail.isAuthor,
                                isLiked = lifehackDetail.isLiked,
                                isSaved = lifehackDetail.isSaved
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
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onOptionsClick() {
        _uiState.update { it.copy(showOptionsContextMenu = true) }
    }

    fun onDismissOptionsMenu() {
        _uiState.update { it.copy(showOptionsContextMenu = false) }
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
                        _uiState.update { it.copy(errorMessage = error.message) }
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
        onNavigate: (Int) -> Unit
    ) {
        uiState.value.lifehack?.category?.let { category ->
            onNavigate(category.id)
        }
    }
}