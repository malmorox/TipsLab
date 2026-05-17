package app.iesjdlc.tipslab.presentation.screens.profile

import app.iesjdlc.tipslab.domain.model.Lifehack

data class ProfileUiState(
    val username: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val posts: List<Lifehack> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
