package app.iesjdlc.tipslab.screens.auth

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import app.iesjdlc.tipslab.viewmodels.SignupEffect
import app.iesjdlc.tipslab.viewmodels.SignupViewModel

@Composable
fun SignupScreen(
    onSignUpSuccess: () -> Unit,
    onSignUpError: (String) -> Unit
) {
    val viewModel: SignupViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.runTestSignupIfNeeded()
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                SignupEffect.NavigateToMain -> onSignUpSuccess()
                is SignupEffect.ShowError -> onSignUpError(effect.message)
            }
        }
    }
}