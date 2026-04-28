package app.iesjdlc.tipslab.presentation.screens.lifehack.edit

import android.net.Uri
import app.iesjdlc.tipslab.core.utils.UiText
import app.iesjdlc.tipslab.domain.model.Category

data class EditLifehackUiState(
    val title: String = "",
    val description: String = "",
    val steps: List<String> = emptyList(),
    val stepsCount: Int = 0,
    val category: Category? = null,
    val availableCategories: List<Category> = emptyList(),
    val mediaUrl: String? = null,
    val mediaLocalUri: Uri? = null,
    val mediaRemoved: Boolean = false,

    val showStepsSheet: Boolean = false,
    val showCategorySheet: Boolean = false,
    val showDiscardChangesDialog: Boolean = false,
    val isLoading: Boolean = false,

    val titleErrorMessage: UiText? = null,
    val descriptionErrorMessage: UiText? = null,
    val categoryErrorMessage: UiText? = null,

    val globalErrorMessage: String? = null
)
