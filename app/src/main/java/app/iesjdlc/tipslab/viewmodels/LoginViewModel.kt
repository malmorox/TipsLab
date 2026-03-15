package app.iesjdlc.tipslab.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val emailOrUsername: String = "",
    val password: String = "",
    val emailOrUsernameError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val canSubmit: Boolean
        get() = emailOrUsername.isNotBlank() && password.isNotBlank() && !isLoading
}

sealed interface LoginEvent {
    data class EmailOrUsernameChanged(val value: String) : LoginEvent
    data class PasswordChanged(val value: String) : LoginEvent
    data object SubmitClicked : LoginEvent
    data object DismissError : LoginEvent
}

sealed interface LoginEffect {
    data object NavigateToMain : LoginEffect
    data class ShowMessage(val message: String) : LoginEffect
}

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effects = Channel<LoginEffect>(Channel.BUFFERED)
    val effects: Flow<LoginEffect> = _effects.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailOrUsernameChanged -> {
                _uiState.update {
                    it.copy(
                        emailOrUsername = event.value,
                        emailOrUsernameError = null,
                        errorMessage = null
                    )
                }
            }

            is LoginEvent.PasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = event.value,
                        passwordError = null,
                        errorMessage = null
                    )
                }
            }

            LoginEvent.SubmitClicked -> submitLogin()

            LoginEvent.DismissError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    private fun submitLogin() {
        val state = _uiState.value
        if (state.isLoading) return

        val emailOrUsernameError = validateEmailOrUsername(state.emailOrUsername)
        val passwordError = validatePassword(state.password)

        if (emailOrUsernameError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    emailOrUsernameError = emailOrUsernameError,
                    passwordError = passwordError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    emailOrUsernameError = null,
                    passwordError = null
                )
            }

            val result = authRepository.login(
                emailOrUsername = state.emailOrUsername.trim(),
                password = state.password
            )

            result.onSuccess {
                _uiState.update { current -> current.copy(isLoading = false) }
                _effects.send(LoginEffect.NavigateToMain)
            }.onFailure { throwable ->
                val message = throwable.message ?: "No se pudo iniciar sesion"
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = message
                    )
                }
                _effects.send(LoginEffect.ShowMessage(message))
            }
        }
    }

    private fun validateEmailOrUsername(value: String): String? {
        if (value.isBlank()) return "El email o usuario es obligatorio"
        return null
    }

    private fun validatePassword(value: String): String? {
        if (value.isBlank()) return "La contraseña es obligatoria"
        if (value.length < 6) return "La contraseña debe tener al menos 6 caracteres"
        return null
    }
}
