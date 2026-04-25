package app.iesjdlc.tipslab.ui.screens.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
	private val signUpUseCase: SignUpUseCase
) : ViewModel() {
	private val _uiState = MutableStateFlow(SignupUiState())
	val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

	fun onEmailChange(newValue: String) {
		_uiState.update {
			it.copy(
				email = newValue,
				errorMessage = null
			)
		}
	}

	fun onUsernameChange(newValue: String) {
		_uiState.update {
			it.copy(
				username = newValue,
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

	fun onConfirmPasswordChange(newValue: String) {
		_uiState.update {
			it.copy(
				confirmPassword = newValue,
				errorMessage = null
			)
		}
	}

	fun onTogglePasswordVisibility() {
		_uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
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

			try {
				signUpUseCase(
					email = currentState.email,
					username = currentState.username,
					password = currentState.password
				)
					.onSuccess {
						onSuccess()
					}
					.onFailure { error ->
						_uiState.update { it.copy(errorMessage = error.message)	}
					}
			} finally {
				_uiState.update { it.copy(isLoading = false) }
			}
		}
	}
}
