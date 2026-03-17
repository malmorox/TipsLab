package app.iesjdlc.tipslab.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
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
                onFinish = {
                    navController.navigate(Routes.AuthGraph.route) {
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
                    onSignUpSuccess = {
                        navController.navigate(Routes.Main.route) {
                            popUpTo(Routes.AuthGraph.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onSignUpError = {
                        navController.popBackStack() // vuelve al Login si falla la creación de prueba
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