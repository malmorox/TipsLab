package app.iesjdlc.tipslab.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun refreshUser() {
        viewModelScope.launch {
            runCatching {
                authRepository.getCurrentUser()
            }.onSuccess { user ->
                _uiState.value = _uiState.value.copy(
                    username = user.username,
                    email = user.email,
                    photoUrl = user.photoUrl
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    errorMessage = it.message
                )
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            runCatching {
                authRepository.getCurrentUser()
            }.onSuccess { user ->
                _uiState.value = _uiState.value.copy(
                    username = user.username,
                    email = user.email,
                    photoUrl = user.photoUrl
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    errorMessage = it.message
                )
            }
        }
    }
    fun onLogout(onSuccess: () -> Unit) {
        authRepository.logout()
        onSuccess()
    }
}