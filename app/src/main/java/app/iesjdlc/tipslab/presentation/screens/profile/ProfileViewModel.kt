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

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val lifehackRepository: LifehackRepository,
    private val savedLikedRepository: SavedLikedRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun refresh() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            runCatching {

                authRepository.getCurrentUser()

            }.onSuccess { user ->

                val posts = lifehackRepository
                    .getUserLifehacks(user.id)
                    .getOrDefault(emptyList())

                val favoritePosts = savedLikedRepository
                    .getUserLikedLifehacks(user.id)
                    .getOrDefault(emptyList())

                val savedPosts = savedLikedRepository
                    .getUserSavedLifehacks(user.id)
                    .getOrDefault(emptyList())

                _uiState.value = _uiState.value.copy(
                    username = user.username,
                    email = user.email,
                    photoUrl = user.photoUrl,

                    posts = posts,
                    favoritePosts = favoritePosts,
                    savedPosts = savedPosts,

                    isLoading = false
                )

            }.onFailure {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
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