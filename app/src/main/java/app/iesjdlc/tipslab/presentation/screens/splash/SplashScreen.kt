package app.iesjdlc.tipslab.presentation.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.iesjdlc.tipslab.R

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToMain: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    LaunchedEffect(viewModel.isLoggedIn) {
        when (viewModel.isLoggedIn) {
            true -> onNavigateToMain()
            false -> onNavigateToLogin()
            null -> Unit // Espera hasta que cargue el estado y muestra la UI
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.tipslab_logo),
            contentDescription = null,
            modifier = Modifier.size(180.dp)
        )
    }
}