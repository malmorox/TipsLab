package app.iesjdlc.tipslab.presentation.screens.profile

data class ProfileUiState(
    val username: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val posts: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
