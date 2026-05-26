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
import app.iesjdlc.tipslab.domain.repository.SavedLikedRepository
import kotlinx.coroutines.flow.update

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val lifehackRepository: LifehackRepository,
    private val savedLikedRepository: SavedLikedRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                authRepository.getCurrentUser()
            }.onSuccess { user ->
                val posts = lifehackRepository
                    .getUserLifehacks(user.id)
                    .getOrDefault(emptyList())

                _uiState.update {
                    it.copy(
                        user = user,
                        posts = posts,
                        isLoading = false
                    )
                }

                launch {
                    savedLikedRepository.observeUserLikedLifehacks(user.id).collect { liked ->
                        _uiState.update { it.copy(favoritePosts = liked) }
                    }
                }

                launch {
                    savedLikedRepository.observeUserSavedLifehacks(user.id).collect { saved ->
                        _uiState.update { it.copy(savedPosts = saved) }
                    }
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.message)
                }
            }
        }
    }

    fun onLogout(onSuccess: () -> Unit) {
        authRepository.logout()
        onSuccess()
    }
}