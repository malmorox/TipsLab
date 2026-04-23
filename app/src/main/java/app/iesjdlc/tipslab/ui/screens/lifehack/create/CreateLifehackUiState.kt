package app.iesjdlc.tipslab.ui.screens.lifehack.create

import android.net.Uri
import app.iesjdlc.tipslab.core.utils.UiText
import app.iesjdlc.tipslab.domain.model.Category

data class CreateLifehackUiState(
    val title: String = "",
    val description: String = "",
    val steps: List<String> = emptyList(),
    val stepsCount: Int = 0,
    val category: Category? = null,
    val allCategories: List<Category> = emptyList(),
    val mediaLocalUri: Uri? = null,

    val showStepsSheet: Boolean = false,
    val showCategorySheet: Boolean = false,
    val showDiscardChangesDialog: Boolean = false,
    val isLoading: Boolean = false,

    val titleErrorMessage: UiText? = null,
    val descriptionErrorMessage: UiText? = null,
    val categoryErrorMessage: UiText? = null,

    val globalErrorMessage: String? = null
)
