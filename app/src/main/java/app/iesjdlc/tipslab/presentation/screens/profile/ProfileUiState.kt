package app.iesjdlc.tipslab.presentation.screens.profile

import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.model.User

data class ProfileUiState(
    val user: User? = null,
    val posts: List<Lifehack> = emptyList(),
    val favoritePosts: List<Lifehack> = emptyList(),
    val savedPosts: List<Lifehack> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
