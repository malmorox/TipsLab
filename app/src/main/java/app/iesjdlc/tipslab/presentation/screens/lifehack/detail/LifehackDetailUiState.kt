package app.iesjdlc.tipslab.presentation.screens.lifehack.detail

import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.presentation.common.UploadState

data class LifehackDetailUiState(
    val lifehack: Lifehack? = null,
    val isLoading: Boolean = true,
    val uploadState: UploadState? = null,
    val isAuthor: Boolean = false,
    val isSaved: Boolean = false,
    val isLiked: Boolean = false,
    val showOptionsContextMenu: Boolean = false,
    val showConfirmDeleteDialog: Boolean = false,
    val errorMessage: String? = null
)