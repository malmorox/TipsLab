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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val lifehackRepository: LifehackRepository,
    private val savedLikedRepository: SavedLikedRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var postsListenerJob: Job? = null
    private var likedListenerJob: Job? = null
    private var savedListenerJob: Job? = null

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                authRepository.getCurrentUser()
            }.onSuccess { user ->
                _uiState.update {
                    it.copy(
                        user = user,
                        isLoading = false
                    )
                }

                postsListenerJob?.cancel()
                likedListenerJob?.cancel()
                savedListenerJob?.cancel()

                postsListenerJob = launch {
                    lifehackRepository.observeUserLifehacks(user.id).collect { posts ->
                        _uiState.update { it.copy(posts = posts) }
                    }
                }

                likedListenerJob = launch {
                    savedLikedRepository.observeUserLikedLifehacks(user.id).collect { liked ->
                        _uiState.update { it.copy(favoritePosts = liked) }
                    }
                }

                savedListenerJob = launch {
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
        postsListenerJob?.cancel()
        likedListenerJob?.cancel()
        savedListenerJob?.cancel()
        postsListenerJob = null
        likedListenerJob = null
        savedListenerJob = null

        authRepository.logout()
        onSuccess()
    }
}