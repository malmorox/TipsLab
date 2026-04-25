package app.iesjdlc.tipslab.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import app.iesjdlc.tipslab.ui.screens.lifehack.edit.EditLifehackScreen
import app.iesjdlc.tipslab.ui.screens.profile.edit.EditProfileScreen
import app.iesjdlc.tipslab.ui.screens.lifehack.detail.LifehackDetailScreen
import app.iesjdlc.tipslab.ui.screens.splash.SplashScreen
import app.iesjdlc.tipslab.ui.screens.auth.login.LoginScreen
import app.iesjdlc.tipslab.ui.screens.auth.signup.SignupScreen
import app.iesjdlc.tipslab.ui.screens.explore.CategoryScreen
import app.iesjdlc.tipslab.ui.screens.explore.SearchScreen
import app.iesjdlc.tipslab.ui.screens.lifehack.create.CreateLifehackScreen

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
            composable<Route.Login>(
                enterTransition = { NavAnimations.slideInFromLeft() },
                exitTransition = { NavAnimations.slideOutToLeft() },
                popEnterTransition = { NavAnimations.slideInFromLeft() }
            ) {
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
            composable<Route.Signup>(
                enterTransition = { NavAnimations.slideInFromRight() },
                popEnterTransition = { NavAnimations.slideInFromRight() }
            ) {
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

        // Pantalla de creación de Lifehack separada por ser un flujo largo
        composable<Route.CreateLifehack>(
            enterTransition = { NavAnimations.slideInVertical() },
            popExitTransition = { NavAnimations.slideOutVertical() }
        ) {
            CreateLifehackScreen(
                onNavigateBack = { rootNavController.popBackStack() },
                onLifehackCreated = { lifehackId ->
                    rootNavController.navigate(Route.LifehackDetail(lifehackId)) {
                        popUpTo<Route.CreateLifehack> { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Pantallas de destino
        composable<Route.Search> {
            SearchScreen(
                onNavigateBack = { rootNavController.popBackStack() }
            )
        }

        composable<Route.LifehackDetail> {
            LifehackDetailScreen(
                onNavigateBack = { rootNavController.popBackStack() },
                onEditLifehack = { lifehackId ->
                    rootNavController.navigate(Route.EditLifehack(lifehackId))
                },
                onDeleteLifehack = { rootNavController.popBackStack() },
                onOpenCategory = { categoryId ->
                    rootNavController.navigate(Route.LifehacksByCategory(categoryId))
                }
            )
        }

        composable<Route.LifehacksByCategory> {
            CategoryScreen(
                onNavigateBack = { rootNavController.popBackStack() },
                onLifehackClick = { lifehackId ->
                    rootNavController.navigate(Route.LifehackDetail(lifehackId))
                }
            )
        }

        composable<Route.EditLifehack> {
            EditLifehackScreen(
                onNavigateBack = { rootNavController.popBackStack() },
                onLifehackEdited = { lifehackId ->
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