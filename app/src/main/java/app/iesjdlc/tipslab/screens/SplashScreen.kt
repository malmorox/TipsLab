package app.iesjdlc.tipslab.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import app.iesjdlc.tipslab.viewmodels.SplashViewModel

@Composable
fun SplashScreen(
    onNavigateToMain: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: SplashViewModel = viewModel()
) {
    LaunchedEffect(viewModel.isLoggedIn) {
        when (viewModel.isLoggedIn) {
            true  -> onNavigateToMain()
            false -> onNavigateToLogin()
            null  -> Unit // Espera hasta que cargue el estado y muestra la UI
        }
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}