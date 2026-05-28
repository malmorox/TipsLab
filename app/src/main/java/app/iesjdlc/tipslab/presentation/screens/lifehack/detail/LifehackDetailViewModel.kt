package app.iesjdlc.tipslab.presentation.screens.lifehack.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.work.WorkInfo
import androidx.work.WorkManager
import app.iesjdlc.tipslab.domain.repository.CommentRepository
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.usecase.comment.AddCommentUseCase
import app.iesjdlc.tipslab.domain.usecase.lifehack.GetLifehackDetailUseCase
import app.iesjdlc.tipslab.domain.usecase.lifehack.ObserveLikedAndSavedUseCase
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
    private val observeLikedAndSavedUseCase: ObserveLikedAndSavedUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val lifehackRepository: LifehackRepository,
    private val commentRepository: CommentRepository,
    private val workManager: WorkManager
) : ViewModel() {
    private val lifehackId = savedStateHandle.toRoute<Route.LifehackDetail>().lifehackId

    private val _uiState = MutableStateFlow(LifehackDetailUiState())
    val uiState: StateFlow<LifehackDetailUiState> = _uiState.asStateFlow()

    init {
        loadLifehack()
        observeLifehack()
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
                                isAuthor = lifehackDetail.isAuthor
                            )
                        }
                        if (!lifehackDetail.isAuthor) {
                            observeLikedAndSaved()
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
        val showing = uiState.value.showComments
        _uiState.update { it.copy(showComments = !showing) }
        if (!showing) observeComments()
    }

    private fun observeComments() {
        viewModelScope.launch {
            commentRepository.observeComments(lifehackId)
                .collect { comments ->
                    _uiState.update { it.copy(comments = comments) }
                }
        }
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

    private fun observeLikedAndSaved() {
        if (uiState.value.isAuthor) return
        viewModelScope.launch {
            observeLikedAndSavedUseCase(lifehackId)
                .collect { (isLiked, isSaved) ->
                    _uiState.update { it.copy(isLiked = isLiked, isSaved = isSaved) }
                }
        }
    }

    fun onLikeClick() {
        if (uiState.value.isAuthor) return
        viewModelScope.launch {
            val wasLiked = uiState.value.isLiked
            _uiState.update { it.copy(isLiked = !wasLiked) }
            toggleLikeUseCase(lifehackId)
                .onFailure { _uiState.update { it.copy(isLiked = wasLiked) } }
        }
    }

    fun onSaveClick() {
        if (uiState.value.isAuthor) return
        viewModelScope.launch {
            val wasSaved = uiState.value.isSaved
            _uiState.update { it.copy(isSaved = !wasSaved) }
            toggleSaveUseCase(lifehackId)
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

    fun onCommentTextChange(text: String) {
        _uiState.update { it.copy(commentText = text) }
    }

    fun onSendComment() {
        viewModelScope.launch {
            val text = uiState.value.commentText.trim()
            if (text.isBlank()) return@launch
            _uiState.update { it.copy(commentText = "") }
            addCommentUseCase(lifehackId, text)
        }
    }

    fun onDeleteComment(commentId: String) {
        viewModelScope.launch {
            commentRepository.deleteComment(lifehackId, commentId)
        }
    }
}