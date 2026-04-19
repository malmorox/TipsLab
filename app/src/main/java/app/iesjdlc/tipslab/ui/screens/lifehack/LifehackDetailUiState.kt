package app.iesjdlc.tipslab.ui.screens.lifehack

import app.iesjdlc.tipslab.domain.model.Lifehack

data class LifehackDetailUiState(
    val lifehack: Lifehack? = null,
    val isLoading: Boolean = true,
    val isOwner: Boolean = false,
    val isSaved: Boolean = false,
    val isLiked: Boolean = false,
    val error: String? = null
)
