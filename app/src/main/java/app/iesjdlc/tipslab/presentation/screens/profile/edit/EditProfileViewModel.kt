package app.iesjdlc.tipslab.presentation.screens.profile.edit

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.data.repository.AuthRepositoryImpl
import app.iesjdlc.tipslab.domain.model.User
import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.MediaRepository
import app.iesjdlc.tipslab.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {

        viewModelScope.launch {

            runCatching {

                authRepository.getCurrentUser()

            }.onSuccess { user ->

                _uiState.value = _uiState.value.copy(
                    email = user.email,
                    username = user.username,
                    photoUrl = user.photoUrl
                )
            }
        }
    }

    fun onEmailChange(email: String) {

        _uiState.value = _uiState.value.copy(
            email = email
        )
    }

    fun onUsernameChange(username: String) {

        _uiState.value = _uiState.value.copy(
            username = username
        )
    }

    fun onPasswordChange(password: String) {

        _uiState.value = _uiState.value.copy(
            password = password
        )
    }

    fun onTogglePasswordVisibility() {

        _uiState.value = _uiState.value.copy(
            isPasswordVisible = !_uiState.value.isPasswordVisible
        )
    }

    fun onProfileImageSelected(uri: Uri) {

        _uiState.value = _uiState.value.copy(
            selectedImageUri = uri
        )
    }

    fun onSaveProfile(onSuccess: () -> Unit) {

        viewModelScope.launch {

            try {

                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    errorMessage = null
                )

                val currentUser = authRepository.getCurrentUser()

                val uploadedPhotoUrl =
                    if (_uiState.value.selectedImageUri != null) {

                        mediaRepository.uploadProfilePhoto(
                            uid = currentUser.id,
                            imageUri = _uiState.value.selectedImageUri!!
                        ).getOrThrow()

                    } else {
                        currentUser.photoUrl
                    }

                val updatedUser = User(
                    id = currentUser.id,
                    email = _uiState.value.email,
                    username = _uiState.value.username,
                    photoUrl = uploadedPhotoUrl,
                    provider = currentUser.provider
                )

                userRepository.updateUser(updatedUser).getOrThrow()

                (authRepository as AuthRepositoryImpl).updateCachedUser(updatedUser)

                _uiState.value = _uiState.value.copy(
                    isLoading = false
                )

                onSuccess()

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
}