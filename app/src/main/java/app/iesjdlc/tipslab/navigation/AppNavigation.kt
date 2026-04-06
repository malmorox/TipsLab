package app.iesjdlc.tipslab.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import app.iesjdlc.tipslab.screens.EditLifehackScreen
import app.iesjdlc.tipslab.screens.EditProfileScreen
import app.iesjdlc.tipslab.screens.LifehackDetailScreen
import app.iesjdlc.tipslab.screens.LifehacksByCategoryScreen
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
                rootNavController = rootNavController,
                onLogout = {
                    rootNavController.navigate(Route.AuthGraph) {
                        popUpTo<Route.MainGraph> { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Pantallas de destino
        composable<Route.LifehackDetail> { backStackEntry ->
            val lifehackId = backStackEntry.toRoute<Route.LifehackDetail>().lifehackId
            LifehackDetailScreen(lifehackId = lifehackId)
        }

        composable<Route.LifehacksByCategory> {
            val categoryId = it.toRoute<Route.LifehacksByCategory>().categoryId
            LifehacksByCategoryScreen(categoryId = categoryId)
        }

        composable<Route.EditLifehack> {
            val lifehackId = it.toRoute<Route.EditLifehack>().lifehackId
            EditLifehackScreen(lifehackId = lifehackId)
        }

        composable<Route.EditProfile> {
            EditProfileScreen()
        }
    }
}

private fun NavController.navigateFromSplash(destination: Route) {
    navigate(destination) {
        popUpTo<Route.Splash> { inclusive = true }
        launchSingleTop = true
    }
}