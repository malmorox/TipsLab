package app.iesjdlc.tipslab.ui.screens.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.data.repository.AuthRepository
import app.iesjdlc.tipslab.data.repository.UserRepositoryImpl
import app.iesjdlc.tipslab.domain.usecase.SignUpUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignupUiState(
	val isLoading: Boolean = false,
	val started: Boolean = false,
	val errorMessage: String? = null
)

sealed interface SignupEffect {
	data object NavigateToMain : SignupEffect
	data class ShowError(val message: String) : SignupEffect
}

class SignupViewModel(
	private val signUpUseCase: SignUpUseCase = SignUpUseCase(
		authRepository = AuthRepository(),
		userRepository = UserRepositoryImpl()
	)
) : ViewModel() {

	private val _uiState = MutableStateFlow(SignupUiState())
	val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

	private val _effects = Channel<SignupEffect>(Channel.BUFFERED)
	val effects: Flow<SignupEffect> = _effects.receiveAsFlow()

	fun runTestSignupIfNeeded() {
		val state = _uiState.value
		if (state.started || state.isLoading) return

		_uiState.update { it.copy(started = true, isLoading = true, errorMessage = null) }

		viewModelScope.launch {
			val seed = System.currentTimeMillis()
			val email = "test_${seed}@tipslab.test"
			val username = "test${seed}"
			val password = "Test1234!"

			signUpUseCase(email = email, username = username, password = password)
				.onSuccess {
					_uiState.update { current -> current.copy(isLoading = false) }
					_effects.send(SignupEffect.NavigateToMain)
				}
				.onFailure { throwable ->
					val message = throwable.message ?: "No se pudo crear el usuario de prueba"
					_uiState.update { current -> current.copy(isLoading = false, errorMessage = message) }
					_effects.send(SignupEffect.ShowError(message))
				}
		}
	}
}

