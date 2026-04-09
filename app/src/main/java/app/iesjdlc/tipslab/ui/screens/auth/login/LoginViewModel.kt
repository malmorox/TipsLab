package app.iesjdlc.tipslab.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.domain.usecase.LoginUseCase
import app.iesjdlc.tipslab.domain.usecase.SignInWithGoogleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase = LoginUseCase(),
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase = SignInWithGoogleUseCase()
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailOrUsernameChanged(newValue: String) {
        _uiState.update { it.copy(emailOrUsername = newValue, errorMessage = null) }
    }

    fun onPasswordChanged(newValue: String) {
        _uiState.update { it.copy(password = newValue, errorMessage = null) }
    }

    fun onLoginClick(
        onSuccess: () -> Unit
    ) {
        val currentState = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            loginUseCase(
                emailOrUsername = currentState.emailOrUsername,
                password = currentState.password
            )
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                .onFailure {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = it.errorMessage)
                    }
                }
        }
    }
}
