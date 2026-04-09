package app.iesjdlc.tipslab.ui.screens.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.domain.usecase.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignupViewModel(
	private val signUpUseCase: SignUpUseCase = SignUpUseCase()
) : ViewModel() {
	private val _uiState = MutableStateFlow(SignupUiState())
	val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

	fun onEmailChanged(newValue: String) {
		_uiState.update { it.copy(email = newValue, errorMessage = null) }
	}

	fun onUsernameChanged(newValue: String) {
		_uiState.update { it.copy(username = newValue, errorMessage = null) }
	}

	fun onPasswordChanged(newValue: String) {
		_uiState.update { it.copy(password = newValue, errorMessage = null) }
	}

	fun onConfirmPasswordChanged(newValue: String) {
		_uiState.update { it.copy(confirmPassword = newValue, errorMessage = null) }
	}

	fun onSignupClick(
		onSuccess: () -> Unit
	) {
		val currentState = _uiState.value

		if (currentState.password != currentState.confirmPassword) {
			_uiState.update { it.copy(errorMessage = "Las contraseñas no coinciden") }
			return
		}

		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }

			signUpUseCase(
				email = currentState.email,
				username = currentState.username,
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
