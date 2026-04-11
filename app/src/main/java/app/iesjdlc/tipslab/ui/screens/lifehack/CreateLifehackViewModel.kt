package app.iesjdlc.tipslab.ui.screens.lifehack

import androidx.lifecycle.ViewModel
import app.iesjdlc.tipslab.domain.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CreateLifehackViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateLifehackUiState())
    val uiState: StateFlow<CreateLifehackUiState> = _uiState.asStateFlow()

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

    fun onSubmit(
        onSuccess: (String) -> Unit
    ) {
        val currentState = _uiState.value

    }
}