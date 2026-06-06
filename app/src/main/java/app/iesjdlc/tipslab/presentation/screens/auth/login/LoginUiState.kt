package app.iesjdlc.tipslab.presentation.screens.auth.login

data class LoginUiState(
    val emailOrUsername: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val googleIsLoading: Boolean = false,
    val errorMessage: String? = null
)