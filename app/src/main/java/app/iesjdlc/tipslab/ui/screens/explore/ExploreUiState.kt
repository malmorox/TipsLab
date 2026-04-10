package app.iesjdlc.tipslab.ui.screens.explore

data class ExploreUiState(
    val emailOrUsername: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
