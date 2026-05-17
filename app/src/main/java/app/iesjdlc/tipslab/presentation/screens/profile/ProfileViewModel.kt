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
import app.iesjdlc.tipslab.domain.repository.LifehackRepository

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val lifehackRepository: LifehackRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun refresh() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

            runCatching {

                val user = authRepository.getCurrentUser()

                val posts = lifehackRepository
                    .getUserLifehacks(user.id)
                    .getOrThrow()
                    .sortedByDescending { it.createdAt }

                user to posts

            }.onSuccess { (user, posts) ->

                _uiState.value = _uiState.value.copy(
                    username = user.username,
                    email = user.email,
                    photoUrl = user.photoUrl,
                    posts = posts,
                    isLoading = false
                )

            }.onFailure {

                _uiState.value = _uiState.value.copy(
                    errorMessage = it.message,
                    isLoading = false
                )
            }
        }
    }
    fun onLogout(onSuccess: () -> Unit) {
        authRepository.logout()
        onSuccess()
    }
}