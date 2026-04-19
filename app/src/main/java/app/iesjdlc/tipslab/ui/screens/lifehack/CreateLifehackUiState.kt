package app.iesjdlc.tipslab.ui.screens.lifehack

import android.net.Uri
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.MediaType

data class CreateLifehackUiState(
    val title: String = "",
    val description: String = "",
    val steps: List<String> = emptyList(),
    val stepsCount: Int = 0,
    val category: Category? = null,
    val availableCategories: List<Category> = emptyList(),
    val mediaLocalUri: Uri? = null,

    val showStepsSheet: Boolean = false,
    val showCategorySheet: Boolean = false,
    val showDiscardChangesDialog: Boolean = false,
    val isLoading: Boolean = false,

    // Errores
    val titleErrorMessage: String? = null,
    val descriptionErrorMessage: String? = null,
    val categoryErrorMessage: String? = null,

    // Error global
    val globalErrorMessage: String? = null
)
