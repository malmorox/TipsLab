package app.iesjdlc.tipslab.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.iesjdlc.tipslab.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    // Cambiar a DI
    authRepository: AuthRepository = AuthRepository()
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
