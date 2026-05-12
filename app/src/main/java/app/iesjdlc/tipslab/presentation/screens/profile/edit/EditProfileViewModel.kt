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
import kotlinx.coroutines.flow.*
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

    private var originalUsername: String = ""
    private var originalPhotoUrl: String? = null

    init {
        loadUser()
    }

    private fun loadUser() {

        viewModelScope.launch {

            runCatching {

                authRepository.getCurrentUser()

            }.onSuccess { user ->
                originalUsername = user.username
                originalPhotoUrl = user.photoUrl

                _uiState.value = _uiState.value.copy(
                    email = user.email,
                    username = user.username,
                    profilePhoto = user.photoUrl
                )
            }
        }
    }

    fun onEmailChange(v: String) {
        _uiState.value = _uiState.value.copy(email = v)
    }

    fun onUsernameChange(v: String) {
        _uiState.value = _uiState.value.copy(username = v)
    }

    fun onProfilePhotoPicked(uri: Uri) {
        _uiState.value = _uiState.value.copy(profilePhoto = uri)
    }

    fun onPhotoRemove() {
        _uiState.value = _uiState.value.copy(profilePhoto = null)
    }

    fun onSave(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        if (!validate(currentState)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, globalErrorMessage = null) }
            try {
                val currentUser = authRepository.getCurrentUser()

                val uploadedPhotoUrl = when (val photo = currentState.profilePhoto) {
                    is Uri -> mediaRepository.uploadProfilePhoto(
                        uid = currentUser.id,
                        imageUri = photo
                    ).getOrThrow()
                    is String -> photo
                    else -> null
                }

                val updatedUser = User(
                    id = currentUser.id,
                    email = currentUser.email,
                    username = currentState.username,
                    photoUrl = uploadedPhotoUrl,
                    provider = currentUser.provider
                )

                userRepository.updateUser(updatedUser).getOrThrow()
                authRepository.loadProfile(currentUser.id)
                onSuccess()

            } catch (e: Exception) {
                _uiState.update { it.copy(globalErrorMessage = e.message) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun validate(state: EditProfileUiState): Boolean {
        var isValid = true

        if (state.username.isBlank()) {
            _uiState.update { it.copy(usernameErrorMessage = "El nombre de usuario es obligatorio") }
            isValid = false
        }

        return isValid
    }

    fun onBackClick(onNavigateBack: () -> Unit) {
        if (hasChanges(_uiState.value)) {
            _uiState.update { it.copy(showDiscardChangesDialog = true) }
        } else {
            onNavigateBack()
        }
    }

    fun onDiscardChangesConfirm(onNavigateBack: () -> Unit) {
        _uiState.update { it.copy(showDiscardChangesDialog = false) }
        onNavigateBack()
    }

    fun onDiscardChangesDismiss() {
        _uiState.update { it.copy(showDiscardChangesDialog = false) }
    }

    private fun hasChanges(state: EditProfileUiState): Boolean {
        return state.username != originalUsername ||
                state.profilePhoto != originalPhotoUrl
    }
}