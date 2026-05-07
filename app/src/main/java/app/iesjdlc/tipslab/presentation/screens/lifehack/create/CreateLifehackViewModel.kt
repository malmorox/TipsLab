package app.iesjdlc.tipslab.presentation.screens.lifehack.create

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.core.constants.FormConstants
import app.iesjdlc.tipslab.core.utils.UiText
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.MediaSource
import app.iesjdlc.tipslab.domain.model.MediaType
import app.iesjdlc.tipslab.domain.repository.CategoryRepository
import app.iesjdlc.tipslab.domain.usecase.lifehack.CreateLifehackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.net.toUri
import app.iesjdlc.tipslab.core.constants.NavConstants
import app.iesjdlc.tipslab.core.model.CameraMediaResult

@HiltViewModel
class CreateLifehackViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val createLifehackUseCase: CreateLifehackUseCase,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateLifehackUiState())
    val uiState: StateFlow<CreateLifehackUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
        observeCameraResult()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val categories = categoryRepository.getAllCategories()
                .getOrElse { emptyList() }
            _uiState.update { it.copy(allCategories = categories) }
        }
    }

    private fun observeCameraResult() {
        viewModelScope.launch {
            savedStateHandle.getStateFlow<CameraMediaResult?>(
                NavConstants.CAMERA_MEDIA_RESULT_KEY,
                null
            )
                .filterNotNull()
                .collect { result ->
                    onMediaPicked(result.uri, result.type)
                    // limpiar para que no se reprocese si vuelve a composar
                    savedStateHandle.remove<CameraMediaResult>(NavConstants.CAMERA_MEDIA_RESULT_KEY)
                }
        }
    }

    fun onTitleChange(newValue: String) {
        _uiState.update {
            it.copy(
                title = newValue,
                titleErrorMessage = null
            )
        }
    }

    fun onDescriptionChange(newValue: String) {
        _uiState.update {
            it.copy(
                description = newValue,
                descriptionErrorMessage = null
            )
        }
    }

    fun onStepsChange(steps: List<String>) {
        _uiState.update {
            it.copy(
                steps = steps,
                stepsCount = steps.count { step -> step.isNotBlank() }
            )
        }
    }

    fun onStepsSheetOpen() {
        _uiState.update { it.copy(showStepsSheet = true) }
    }

    fun onStepsSheetDismiss() {
        _uiState.update { it.copy(showStepsSheet = false) }
    }

    fun onCategoryChange(newValue: Category) {
        _uiState.update {
            it.copy(
                category = newValue,
                categoryErrorMessage = null,
                showCategorySheet = false
            )
        }
    }

    fun onCategorySheetOpen() {
        _uiState.update { it.copy(showCategorySheet = true) }
    }

    fun onCategorySheetDismiss() {
        _uiState.update { it.copy(showCategorySheet = false) }
    }

    fun onMediaPicked(uri: Uri, type: MediaType) {
        _uiState.update { it.copy(mediaSource = MediaSource.Local(uri, type)) }
    }

    fun onMediaRemove() {
        _uiState.update { it.copy(mediaSource = null) }
    }

    fun onSubmit(
        onSuccess: (String) -> Unit
    ) {
        val currentState = _uiState.value
        if (!validate(currentState)) return

        val localMedia = currentState.mediaSource as? MediaSource.Local

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, globalErrorMessage = null) }

            try {
                createLifehackUseCase(
                    title = currentState.title,
                    description = currentState.description,
                    category = currentState.category!!,
                    steps = currentState.steps,
                    mediaUri = localMedia?.uri,
                    mediaType = localMedia?.type
                )
                    .onSuccess { lifehackId ->
                        onSuccess(lifehackId)
                    }
                    .onFailure { error ->
                        _uiState.update { it.copy(globalErrorMessage = error.message) }
                    }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun validate(state: CreateLifehackUiState): Boolean {
        var isValid = true

        if (state.title.isBlank()) {
            _uiState.update { it.copy(titleErrorMessage = UiText.StringRes(R.string.title_required)) }
            isValid = false
        }

        if (state.description.isBlank()) {
            _uiState.update { it.copy(descriptionErrorMessage = UiText.StringRes(R.string.description_required)) }
            isValid = false
        } else if (state.description.length < FormConstants.MIN_DESCRIPTION_LENGTH) {
            _uiState.update { it.copy(descriptionErrorMessage = UiText.StringResWithArgs(R.string.description_min_length_required, FormConstants.MIN_DESCRIPTION_LENGTH)) }
            isValid = false
        }

        if (state.category == null) {
            _uiState.update { it.copy(categoryErrorMessage = UiText.StringRes(R.string.category_required)) }
            isValid = false
        }

        return isValid
    }

    fun onCloseClick(
        onNavigateBack: () -> Unit
    ) {
        val currentState = _uiState.value
        if (hasContent(currentState)) {
            _uiState.update { it.copy(showDiscardChangesDialog = true) }
        } else {
            onNavigateBack()
        }
    }

    fun onDiscardChangesConfirm(
        onNavigateBack: () -> Unit
    ) {
        _uiState.update { it.copy(showDiscardChangesDialog = false) }
        onNavigateBack()
    }

    fun onDiscardChangesDismiss() {
        _uiState.update { it.copy(showDiscardChangesDialog = false) }
    }

    private fun hasContent(state: CreateLifehackUiState): Boolean {
        return state.title.isNotBlank() ||
                state.description.isNotBlank() ||
                state.category != null ||
                state.steps.isNotEmpty() ||
                state.mediaSource != null
    }
}