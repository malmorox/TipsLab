package app.iesjdlc.tipslab.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun SignupScreen(
    onSignUp: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(email, { email = it }, label = { Text("Email") })
        OutlinedTextField(pass, { pass = it }, label = { Text("Password") })

        Button(onClick = {
            true
            onSignUp()
        }) {
            Text("Crear cuenta")
        }

        TextButton(onClick = onBackToLogin) { Text("Volver") }
    }
}