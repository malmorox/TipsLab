package app.iesjdlc.tipslab.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import app.iesjdlc.tipslab.screens.SplashScreen
import app.iesjdlc.tipslab.screens.auth.LoginScreen
import app.iesjdlc.tipslab.screens.auth.SignupScreen

@Composable
fun AppNavigation(){
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = Route.MainGraph
    ) {
        // Pantalla de Splash
        composable<Route.Splash> {
            SplashScreen(
                onNavigateToMain = { rootNavController.navigateFromSplash(Route.MainGraph) },
                onNavigateToLogin = { rootNavController.navigateFromSplash(Route.AuthGraph) }
            )
        }

        // Navegación entre pantallas de autenticación
        navigation<Route.AuthGraph>(startDestination = Route.Login) {

            // Pantalla de Login
            composable<Route.Login> {
                LoginScreen(
                    onLoginSuccess = {
                        rootNavController.navigate(Route.MainGraph) {
                            popUpTo<Route.AuthGraph> { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onLoginError = {
                        // En este flujo de prueba no mostramos UI de error.
                    }
                )
            }

            // Pantalla de SignUp
            composable<Route.Signup> {
                SignupScreen(
                    onSignUpSuccess = {
                        rootNavController.navigate(Route.MainGraph) {
                            popUpTo<Route.AuthGraph> { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateBack = {
                        rootNavController.popBackStack() // vuelve al Login si falla la creación de prueba
                    }
                )
            }
        }

        // Navegación entre pantallas una vez autenticado
        composable<Route.MainGraph> {
            MainScaffold(
                onLogout = {
                    rootNavController.navigate(Route.AuthGraph) {
                        popUpTo<Route.MainGraph> { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

private fun NavController.navigateFromSplash(destination: Route) {
    navigate(destination) {
        popUpTo<Route.Splash> { inclusive = true }
        launchSingleTop = true
    }
}