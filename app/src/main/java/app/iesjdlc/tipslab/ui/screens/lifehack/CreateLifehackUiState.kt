package app.iesjdlc.tipslab.ui.screens.lifehack

import android.net.Uri
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.MediaType

data class CreateLifehackUiState(
    val title: String = "",
    val description: String = "",
    val steps: List<String> = emptyList(),
    val category: Category? = null,
    val availableCategories: List<Category> = emptyList(),

    val mediaLocalUri: Uri? = null,
    val mediaType: MediaType? = null,

    val isStepsSheetOpen: Boolean = false,
    val isCategorySheetOpen: Boolean = false,
    val isLoading: Boolean = false,

    // Errores
    val titleErrorMessage: String? = null,
    val descriptionErrorMessage: String? = null,
    val categoryErrorMessage: String? = null,

    // Error global
    val globalErrorMessage: String? = null
)
