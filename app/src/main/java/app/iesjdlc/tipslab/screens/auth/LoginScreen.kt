package app.iesjdlc.tipslab.screens.auth

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.PublicKeyCredential
import androidx.lifecycle.viewmodel.compose.viewModel
import app.iesjdlc.tipslab.viewmodels.LoginEffect
import app.iesjdlc.tipslab.viewmodels.LoginViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onLoginError: (String) -> Unit
) {
    val viewModel: LoginViewModel = viewModel()
    val context = LocalContext.current

    // Efecto que lanza el flujo de Google al abrir la pantalla
    LaunchedEffect(Unit) {
        try {
            val credentialManager = CredentialManager.create(context)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("177275042936-95u9er25hp4utb2dbohv0aluthi8esrp.apps.googleusercontent.com") // el de Firebase Console > Auth > Google > Web client ID
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context as Activity
            )

            val credential = result.credential
            if (credential is PublicKeyCredential) return@LaunchedEffect

            val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val idToken = googleCredential.idToken

            Log.d("TEST_TOKEN", "Token: $idToken") // Por si quieres copiarlo
            viewModel.signInWithGoogle(idToken)

        } catch (e: Exception) {
            onLoginError(e.message ?: "Error Google Sign-In")
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                LoginEffect.NavigateToMain -> onLoginSuccess()
                is LoginEffect.ShowMessage -> onLoginError(effect.message)
            }
        }
    }
}