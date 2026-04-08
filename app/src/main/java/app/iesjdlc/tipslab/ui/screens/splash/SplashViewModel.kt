package app.iesjdlc.tipslab.ui.screens.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    // Cambiar a DI
    authRepository: AuthRepositoryImpl = AuthRepositoryImpl()
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