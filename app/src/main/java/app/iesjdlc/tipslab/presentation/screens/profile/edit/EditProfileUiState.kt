package app.iesjdlc.tipslab.presentation.screens.profile.edit

data class EditProfileUiState(
    val email: String = "",
    val isEmailEditable: Boolean = true,
    val username: String = "",
    val profilePhoto: Any? = null,

    val showDiscardChangesDialog: Boolean = false,
    val isLoading: Boolean = false,

    val emailErrorMessage: String? = null,
    val usernameErrorMessage: String? = null,
    val globalErrorMessage: String? = null
)