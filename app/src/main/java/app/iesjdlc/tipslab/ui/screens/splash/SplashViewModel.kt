package app.iesjdlc.tipslab.ui.screens.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    authRepository: AuthRepository
) : ViewModel() {
    var isLoggedIn by mutableStateOf<Boolean?>(null)
        private set

    init {
        viewModelScope.launch {
            val result = authRepository.checkUserSession()
            delay(2000)
            isLoggedIn = result
        }
    }
}