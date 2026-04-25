package app.iesjdlc.tipslab.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileTab(
    viewModel: ProfileViewModel = hiltViewModel(),
    onEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileTabUI(
        state = uiState,
        onEditProfile = onEditProfile,
        onLogout = { viewModel.onLogout(onLogout) }
    )
}

@Composable
private fun ProfileTabUI(
    state: ProfileUiState,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onLogout) {
            Text(text = "Cerrar Sesión")
        }
    }
}