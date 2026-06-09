package app.iesjdlc.tipslab.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import app.iesjdlc.tipslab.core.constants.NavConstants
import app.iesjdlc.tipslab.core.model.CameraMediaResult
import app.iesjdlc.tipslab.presentation.screens.lifehack.edit.EditLifehackScreen
import app.iesjdlc.tipslab.presentation.screens.profile.edit.EditProfileScreen
import app.iesjdlc.tipslab.presentation.screens.lifehack.detail.LifehackDetailScreen
import app.iesjdlc.tipslab.presentation.screens.splash.SplashScreen
import app.iesjdlc.tipslab.presentation.screens.auth.login.LoginScreen
import app.iesjdlc.tipslab.presentation.screens.auth.signup.SignupScreen
import app.iesjdlc.tipslab.presentation.screens.camera.CameraScreen
import app.iesjdlc.tipslab.presentation.screens.category.CategoryScreen
import app.iesjdlc.tipslab.presentation.screens.explore.SearchScreen
import app.iesjdlc.tipslab.presentation.screens.lifehack.create.CreateLifehackScreen

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
                    onSignupSuccess = {
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
        ) { backStackEntry ->
            val cameraResult by backStackEntry.savedStateHandle
                .getStateFlow<CameraMediaResult?>(NavConstants.CAMERA_MEDIA_RESULT_KEY, null)
                .collectAsStateWithLifecycle()

            CreateLifehackScreen(
                onNavigateBack = { rootNavController.popBackStack() },
                onLifehackCreated = { lifehackId ->
                    rootNavController.navigate(Route.LifehackDetail(lifehackId)) {
                        popUpTo<Route.CreateLifehack> { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onOpenCamera = {
                    rootNavController.navigate(Route.Camera())
                },
                cameraResult = cameraResult,
                onCameraResultConsumed = {
                    backStackEntry.savedStateHandle.remove<CameraMediaResult>(NavConstants.CAMERA_MEDIA_RESULT_KEY)
                }
            )
        }

        composable<Route.Camera>(

        ){
            CameraScreen(
                onMediaCaptured = { uri, type ->
                    rootNavController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            NavConstants.CAMERA_MEDIA_RESULT_KEY,
                            CameraMediaResult(uri, type)
                        )
                    rootNavController.popBackStack()
                },
                onNavigateBack = { rootNavController.popBackStack() }
            )
        }

        // Pantallas de destino
        composable<Route.Search> {
            SearchScreen(
                onNavigateBack = { rootNavController.popBackStack() },
                onOpenLifehack = { lifehackId ->
                    rootNavController.navigate(Route.LifehackDetail(lifehackId)) {
                        launchSingleTop = true
                    }
                },
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
                    rootNavController.navigate(Route.Category(categoryId)) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Route.Category> {
            CategoryScreen(
                onNavigateBack = { rootNavController.popBackStack() },
                onOpenLifehack = { lifehackId ->
                    rootNavController.navigate(Route.LifehackDetail(lifehackId)) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Route.EditLifehack> { backStackEntry ->
            val cameraResult by backStackEntry.savedStateHandle
                .getStateFlow<CameraMediaResult?>(NavConstants.CAMERA_MEDIA_RESULT_KEY, null)
                .collectAsStateWithLifecycle()

            EditLifehackScreen(
                onNavigateBack = { rootNavController.popBackStack() },
                onLifehackEdited = { lifehackId ->
                    rootNavController.navigate(Route.LifehackDetail(lifehackId)) {
                        popUpTo<Route.EditLifehack> { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onOpenCamera = {
                    rootNavController.navigate(Route.Camera())
                },
                cameraResult = cameraResult,
                onCameraResultConsumed = {
                    backStackEntry.savedStateHandle.remove<CameraMediaResult>(NavConstants.CAMERA_MEDIA_RESULT_KEY)
                }
            )
        }

        composable<Route.EditProfile> { backStackEntry ->
            val cameraResult by backStackEntry.savedStateHandle
                .getStateFlow<CameraMediaResult?>(NavConstants.CAMERA_MEDIA_RESULT_KEY, null)
                .collectAsStateWithLifecycle()

            EditProfileScreen(
                onNavigateBack = { rootNavController.popBackStack() },
                onProfileEdited = { rootNavController.popBackStack() },
                onOpenCamera = {
                    rootNavController.navigate(Route.Camera(allowVideo = false))
                },
                cameraResult = cameraResult,
                onCameraResultConsumed = {
                    backStackEntry.savedStateHandle.remove<CameraMediaResult>(NavConstants.CAMERA_MEDIA_RESULT_KEY)
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