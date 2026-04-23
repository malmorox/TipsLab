package app.iesjdlc.tipslab.ui.screens.profile.edit

data class EditProfileUiState(
    val emailOrUsername: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)