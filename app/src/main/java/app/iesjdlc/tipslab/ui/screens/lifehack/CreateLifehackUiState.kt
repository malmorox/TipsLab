package app.iesjdlc.tipslab.ui.screens.lifehack

data class CreateLifehackUiState(
    val emailOrUsername: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
