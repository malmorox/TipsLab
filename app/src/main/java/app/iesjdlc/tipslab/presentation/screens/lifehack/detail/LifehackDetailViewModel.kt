package app.iesjdlc.tipslab.presentation.screens.lifehack.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.work.WorkInfo
import androidx.work.WorkManager
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.usecase.lifehack.GetLifehackDetailUseCase
import app.iesjdlc.tipslab.domain.usecase.lifehack.ToggleLikeLifehackUseCase
import app.iesjdlc.tipslab.domain.usecase.lifehack.ToggleSaveLifehackUseCase
import app.iesjdlc.tipslab.presentation.common.UploadState
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
    private val toggleLikeUseCase: ToggleLikeLifehackUseCase,
    private val toggleSaveUseCase: ToggleSaveLifehackUseCase,
    private val lifehackRepository: LifehackRepository,
    private val workManager: WorkManager
) : ViewModel() {
    private val lifehackId = savedStateHandle.toRoute<Route.LifehackDetail>().lifehackId

    private val _uiState = MutableStateFlow(LifehackDetailUiState())
    val uiState: StateFlow<LifehackDetailUiState> = _uiState.asStateFlow()

    init {
        loadLifehack()
        observeLifehack()
        observeComments()
        observeMediaUploadState()
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

    private fun observeLifehack() {
        viewModelScope.launch {
            lifehackRepository.observeLifehack(lifehackId)
                .collect { lifehack ->
                    _uiState.update { it.copy(lifehack = lifehack) }
                }
        }
    }

    fun onShowComments() {
        _uiState.update { it.copy(showComments = true) }
        observeComments()
    }

    private fun observeComments() {
        // TODO observar comentarios
    }

    private fun observeMediaUploadState() {
        viewModelScope.launch {
            workManager.getWorkInfosByTagFlow(lifehackId)
                .collect { workInfos ->
                    val work = workInfos.firstOrNull()
                    val uploadState = when (work?.state) {
                        WorkInfo.State.ENQUEUED,
                        WorkInfo.State.RUNNING -> UploadState.Loading
                        WorkInfo.State.SUCCEEDED -> UploadState.Success // TODO mostrar que se ha subido el media
                        WorkInfo.State.FAILED -> UploadState.Error
                        else -> null
                    }
                    _uiState.update { it.copy(uploadState = uploadState) }
                }
        }
    }

    fun onLikeClick() {
        viewModelScope.launch {
            val wasLiked = uiState.value.isLiked
            _uiState.update { it.copy(isLiked = !wasLiked) }
            toggleLikeUseCase(lifehackId)
                .onSuccess { newIsLiked -> _uiState.update { it.copy(isLiked = newIsLiked) } }
                .onFailure { _uiState.update { it.copy(isLiked = wasLiked) } }
        }
    }

    fun onSaveClick() {
        viewModelScope.launch {
            val wasSaved = uiState.value.isSaved
            _uiState.update { it.copy(isSaved = !wasSaved) }
            toggleSaveUseCase(lifehackId)
                .onSuccess { newIsSaved -> _uiState.update { it.copy(isSaved = newIsSaved) } }
                .onFailure { _uiState.update { it.copy(isSaved = wasSaved) } }
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