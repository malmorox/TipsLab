package app.iesjdlc.tipslab.ui.screens.lifehack

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.MediaType
import app.iesjdlc.tipslab.domain.repository.CategoryRepository
import app.iesjdlc.tipslab.domain.usecase.CreateLifehackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateLifehackViewModel @Inject constructor(
    private val createLifehackUseCase: CreateLifehackUseCase,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateLifehackUiState())
    val uiState: StateFlow<CreateLifehackUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val categories = categoryRepository.getAllCategories()
                .getOrElse { emptyList() }
            _uiState.update { it.copy(availableCategories = categories) }
        }
    }

    fun onTitleChanged(newValue: String) {
        _uiState.update { it.copy(title = newValue, titleErrorMessage = null) }
    }

    fun onDescriptionChanged(newValue: String) {
        _uiState.update { it.copy(description = newValue, descriptionErrorMessage = null) }
    }

    fun onCategoryChanged(newValue: Category) {
        _uiState.update { it.copy(category = newValue, categoryErrorMessage = null, isCategoryDropdownExpanded = false) }
    }

    fun onCategoryDropdownToggle() {
        _uiState.update { it.copy(isCategoryDropdownExpanded = !it.isCategoryDropdownExpanded) }
    }

    fun onMediaPicked(uri: Uri, type: MediaType) {
        _uiState.update { it.copy(mediaLocalUri = uri, mediaType = type) }
    }

    fun onMediaRemoved() {
        _uiState.update { it.copy(mediaLocalUri = null, mediaType = null) }
    }

    fun onSubmit(
        onSuccess: (String) -> Unit
    ) {
        val currentState = _uiState.value
        if (!validate(currentState)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, globalErrorMessage = null) }

            try {
                createLifehackUseCase(
                    title = currentState.title,
                    description = currentState.description,
                    category = currentState.category!!,
                    mediaUri = currentState.mediaLocalUri,
                    mediaType = currentState.mediaType
                )
                    .onSuccess { lifehackId ->
                        onSuccess(lifehackId)
                    }
                    .onFailure { error ->
                        _uiState.update {
                            it.copy(globalErrorMessage = error.message)
                        }
                    }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun validate(state: CreateLifehackUiState): Boolean {
        var isValid = true

        if (state.title.isBlank()) {
            _uiState.update { it.copy(titleErrorMessage = "El título es obligatorio") }
            isValid = false
        }

        if (state.description.isBlank()) {
            _uiState.update { it.copy(descriptionErrorMessage = "La descripción es obligatoria") }
            isValid = false
        }

        if (state.category == null) {
            _uiState.update { it.copy(categoryErrorMessage = "Selecciona una categoría") }
            isValid = false
        }

        return isValid
    }
}