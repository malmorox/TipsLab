package app.iesjdlc.tipslab.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import app.iesjdlc.tipslab.screens.SplashScreen
import app.iesjdlc.tipslab.screens.auth.LoginScreen
import app.iesjdlc.tipslab.screens.auth.SignupScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        // Pantalla de Splash
        composable(Routes.Splash.route) {
            SplashScreen(
                onFinish = { isLogged ->
                    navController.navigate(
                        if (isLogged) Routes.Main.route else Routes.AuthGraph.route
                    ) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Navegación entre pantallas de autenticación
        navigation(
            route = Routes.AuthGraph.route,
            startDestination = Routes.Login.route
        ) {

            // Pantalla de Login
            composable(Routes.Login.route) {
                LoginScreen(
                    onGoToSignup = {
                        navController.navigate(Routes.Signup.route) {
                            launchSingleTop = true
                        }
                    },
                    onLogin = {
                        navController.navigate(Routes.Main.route) {
                            popUpTo(Routes.AuthGraph.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Pantalla de SignUp
            composable(Routes.Signup.route) {
                SignupScreen(
                    onSignUp = {
                        navController.navigate(Routes.Main.route) {
                            popUpTo(Routes.AuthGraph.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onBackToLogin = {
                        navController.popBackStack() // vuelve al Login
                    }
                )
            }
        }

        // Navegación entre pantallas una vez autenticado
        composable(Routes.Main.route) {
            MainScaffold(rootNavController = navController)
        }
    }
}