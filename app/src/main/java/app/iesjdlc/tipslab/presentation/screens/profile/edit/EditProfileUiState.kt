package app.iesjdlc.tipslab.presentation.screens.profile.edit

import android.net.Uri

data class EditProfileUiState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val photoUrl: String? = null,
    val selectedImageUri: Uri? = null,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)