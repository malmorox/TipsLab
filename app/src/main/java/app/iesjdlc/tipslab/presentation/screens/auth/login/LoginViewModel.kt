package app.iesjdlc.tipslab.presentation.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.domain.usecase.auth.LoginUseCase
import app.iesjdlc.tipslab.domain.usecase.auth.SignInWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailOrUsernameChange(newValue: String) {
        _uiState.update {
            it.copy(
                emailOrUsername = newValue,
                errorMessage = null
            )
        }
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update {
            it.copy(
                password = newValue,
                errorMessage = null
            )
        }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick(
        onSuccess: () -> Unit
    ) {
        val currentState = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                loginUseCase(
                    emailOrUsername = currentState.emailOrUsername,
                    password = currentState.password
                )
                    .onSuccess {
                        onSuccess()
                    }
                    .onFailure { error ->
                        _uiState.update { it.copy(errorMessage = error.message) }
                    }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onContinueWithGoogleClick(
        idToken: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(googleIsLoading = true) }

            try {
                signInWithGoogleUseCase(idToken)
                    .onSuccess {
                        onSuccess()
                    }
                    .onFailure { error ->
                        _uiState.update { it.copy(errorMessage = error.message) }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
