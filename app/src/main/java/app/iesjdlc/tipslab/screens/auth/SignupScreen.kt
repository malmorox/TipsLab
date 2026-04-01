package app.iesjdlc.tipslab.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.viewmodels.SignupEffect
import app.iesjdlc.tipslab.viewmodels.SignupViewModel

@Composable
fun SignupScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateBack: (String) -> Unit
) {
    val viewModel: SignupViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.runTestSignupIfNeeded()
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                SignupEffect.NavigateToMain -> onSignUpSuccess()
                is SignupEffect.ShowError -> onNavigateBack(effect.message)
            }
        }
    }
}

@Composable
fun SignUpScreenUI() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BackgroundApp,
                        BrandLight.copy(alpha = 0.2f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = SurfaceCard
            ),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    tint = BrandPrimary,
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.createAccount),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandPrimary
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Email
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(text = stringResource(id = R.string.email),color = TextSecondary)
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandPrimary,
                        unfocusedBorderColor = TextSecondary.copy(alpha = 0.4f),
                        cursorColor = BrandPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // User
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(text = stringResource(id = R.string.user), color = TextSecondary)
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandPrimary,
                        unfocusedBorderColor = TextSecondary.copy(alpha = 0.4f),
                        cursorColor = BrandPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Contraseña
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(text = stringResource(id = R.string.password), color = TextSecondary)
                    },
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandPrimary,
                        unfocusedBorderColor = TextSecondary.copy(alpha = 0.4f),
                        cursorColor = BrandPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Repetir contraseña
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(text = stringResource(id = R.string.repeatPsswd), color = TextSecondary)
                    },
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandPrimary,
                        unfocusedBorderColor = TextSecondary.copy(alpha = 0.4f),
                        cursorColor = BrandPrimary
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Botón Login con degradado
                Button(
                    onClick = {  },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        BrandDeep,
                                        BrandPrimary,
                                        BrandLight
                                    )
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.signup),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}