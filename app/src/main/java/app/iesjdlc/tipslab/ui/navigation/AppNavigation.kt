package app.iesjdlc.tipslab.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import app.iesjdlc.tipslab.ui.screens.lifehack.EditLifehackScreen
import app.iesjdlc.tipslab.ui.screens.profile.EditProfileScreen
import app.iesjdlc.tipslab.ui.screens.lifehack.LifehackDetailScreen
import app.iesjdlc.tipslab.ui.screens.explore.LifehacksByCategoryScreen
import app.iesjdlc.tipslab.ui.screens.splash.SplashScreen
import app.iesjdlc.tipslab.ui.screens.auth.login.LoginScreen
import app.iesjdlc.tipslab.ui.screens.auth.signup.SignupScreen

@Composable
fun AppNavigation() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = Route.Splash
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
                    onNavigateToSignup = {
                        rootNavController.navigate(Route.Signup) {
                            launchSingleTop = true
                        }
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
                        rootNavController.popBackStack()
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
            LifehackDetailScreen(
                lifehackId = lifehackId,
                onNavigateBack = { rootNavController.popBackStack() },
                onEditLifehack = { rootNavController.navigate(Route.EditLifehack(lifehackId)) },
                onDeleteLifehack = { /* TODO implementar lógica de borrado */ },
                onOpenCategory = { categoryId ->
                    rootNavController.navigate(Route.LifehacksByCategory(categoryId))
                }
            )
        }

        composable<Route.LifehacksByCategory> {
            val categoryId = it.toRoute<Route.LifehacksByCategory>().categoryId
            LifehacksByCategoryScreen(
                categoryId = categoryId,
                onNavigateBack = { rootNavController.popBackStack() },
                onLifehackClick = { id -> rootNavController.navigate(Route.LifehackDetail(id)) }
            )
        }

        composable<Route.EditLifehack> {
            val lifehackId = it.toRoute<Route.EditLifehack>().lifehackId
            EditLifehackScreen(
                lifehackId = lifehackId,
                onNavigateBack = { rootNavController.popBackStack() },
                onLifehackEdited = {
                    rootNavController.navigate(Route.LifehackDetail(lifehackId)) {
                        popUpTo<Route.EditLifehack> { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Route.EditProfile> {
            EditProfileScreen(
                onNavigateBack = { rootNavController.popBackStack() }
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