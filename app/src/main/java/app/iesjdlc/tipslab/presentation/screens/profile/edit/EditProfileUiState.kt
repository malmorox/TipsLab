package app.iesjdlc.tipslab.presentation.screens.profile.edit

import android.net.Uri

data class EditProfileUiState(
    val email: String = "",
    val username: String = "",
    val profilePhoto: Any? = null,

    val showDiscardChangesDialog: Boolean = false,
    val isLoading: Boolean = false,

    val emailErrorMessage: String? = null,
    val usernameErrorMessage: String? = null,
    val globalErrorMessage: String? = null
)