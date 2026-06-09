package app.iesjdlc.tipslab.presentation.screens.lifehack.edit

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.core.constants.FormConstants
import app.iesjdlc.tipslab.core.model.CameraMediaResult
import app.iesjdlc.tipslab.core.utils.UiText
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.MediaSource
import app.iesjdlc.tipslab.domain.model.MediaType
import app.iesjdlc.tipslab.domain.repository.CategoryRepository
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.usecase.lifehack.EditLifehackUseCase
import app.iesjdlc.tipslab.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditLifehackViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val editLifehackUseCase: EditLifehackUseCase,
    private val lifehackRepository: LifehackRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val lifehackId = savedStateHandle.toRoute<Route.EditLifehack>().lifehackId

    private val _uiState = MutableStateFlow(EditLifehackUiState())
    val uiState: StateFlow<EditLifehackUiState> = _uiState.asStateFlow()

    private var originalTitle: String = ""
    private var originalDescription: String = ""
    private var originalSteps: List<String> = emptyList()
    private var originalCategory: Category? = null
    private var originalMediaSource: MediaSource? = null

    init {
        loadLifehack()
        loadCategories()
    }

    private fun loadLifehack() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                lifehackRepository.getLifehackById(lifehackId)
                    .onSuccess { lifehack ->
                        originalTitle = lifehack.title
                        originalDescription = lifehack.description
                        originalSteps = lifehack.steps
                        originalCategory = lifehack.category
                        originalMediaSource = lifehack.media?.let { media ->
                            MediaSource.Remote(media.url, media.type)
                        }

                        _uiState.update {
                            it.copy(
                                title = originalTitle,
                                description = originalDescription,
                                steps = originalSteps,
                                category = originalCategory,
                                mediaSource = originalMediaSource
                            )
                        }
                    }.onFailure { error ->
                        // TODO mostrar error
                    }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val categories = categoryRepository.getAllCategories()
                    .getOrElse { emptyList() }
                _uiState.update { it.copy(allCategories = categories) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
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

    fun onCameraResult(result: CameraMediaResult) {
        onMediaPicked(result.uri, result.type)
    }

    fun onMediaPicked(uri: Uri, type: MediaType) {
        _uiState.update { it.copy(mediaSource = MediaSource.Local(uri, type)) }
    }

    fun onMediaRemove() {
        _uiState.update { it.copy(mediaSource = null) }
    }

    fun onSave(
        onSuccess: (String) -> Unit
    ) {
        val currentState =_uiState.value
        if (!validate(currentState)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val (mediaUri, mediaType) = when (val source = currentState.mediaSource) {
                    is MediaSource.Local -> source.uri to source.type
                    is MediaSource.Remote -> null to null
                    null -> null to null
                }

                editLifehackUseCase(
                    id = lifehackId,
                    title = currentState.title,
                    description = currentState.description,
                    steps = currentState.steps.filter { step -> step.isNotBlank() },
                    category = currentState.category!!,
                    mediaUri = mediaUri,
                    mediaType = mediaType
                ).onSuccess { id ->
                    onSuccess(id)
                }.onFailure { error ->
                    // TODO mostrar error
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun validate(state: EditLifehackUiState): Boolean {
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

    fun onBackClick(
        onNavigateBack: () -> Unit
    ) {
        val currentState = _uiState.value
        if (hasChanges(currentState)) {
            _uiState.update { it.copy(showDiscardChangesDialog = true) }
        } else {
            onNavigateBack()
        }
    }

    fun onDiscardChangesConfirm(
        onNavigateBack: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(showDiscardChangesDialog = false) }
            delay(150)
            onNavigateBack()
        }
    }

    fun onDiscardChangesDismiss() {
        _uiState.update { it.copy(showDiscardChangesDialog = false) }
    }

    private fun hasChanges(state: EditLifehackUiState): Boolean {
        return state.title != originalTitle ||
                state.description != originalDescription ||
                state.steps != originalSteps ||
                state.category?.id != originalCategory?.id ||
                state.mediaSource != originalMediaSource
    }
}