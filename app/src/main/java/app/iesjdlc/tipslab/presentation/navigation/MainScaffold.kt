package app.iesjdlc.tipslab.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.iesjdlc.tipslab.presentation.components.BottomNavBar
import app.iesjdlc.tipslab.presentation.screens.explore.ExploreTab
import app.iesjdlc.tipslab.presentation.screens.home.HomeTab
import app.iesjdlc.tipslab.presentation.screens.profile.ProfileTab

@Composable
fun MainScaffold(
	rootNavController: NavController,
	onLogout: () -> Unit
) {
	val innerNavController = rememberNavController()

	Scaffold(
		bottomBar = {
			BottomNavBar(
				navController = innerNavController,
				onCreate = {
					rootNavController.navigate(Route.CreateLifehack)
				}
			)
		}
	) { innerPadding ->
		NavHost(
			navController = innerNavController,
			startDestination = Route.Home,
			modifier = Modifier.padding(innerPadding)
		) {
			composable<Route.Home>(
				enterTransition = { EnterTransition.None },
				exitTransition = { ExitTransition.None },
				popEnterTransition = { EnterTransition.None },
				popExitTransition = { ExitTransition.None }
			) {
				HomeTab(
					onOpenLifehack = { lifehackId ->
						rootNavController.navigate(Route.LifehackDetail(lifehackId))
					},
					onSearch = {
						rootNavController.navigate(Route.Search)
					}
				)
			}

			composable<Route.Explore>(
				enterTransition = { EnterTransition.None },
				exitTransition = { ExitTransition.None },
				popEnterTransition = { EnterTransition.None },
				popExitTransition = { ExitTransition.None }
			) {
				ExploreTab(
					onOpenCategory = { categoryId ->
						rootNavController.navigate(Route.Category(categoryId))
					},
					onSearch = {
						rootNavController.navigate(Route.Search)
					}
				)
			}

			composable<Route.Profile>(
				enterTransition = { EnterTransition.None },
				exitTransition = { ExitTransition.None },
				popEnterTransition = { EnterTransition.None },
				popExitTransition = { ExitTransition.None }
			) {
				ProfileTab(
					onEditProfile = {
						rootNavController.navigate(Route.EditProfile)
					},
					onOpenLifehack = { lifehackId ->
						rootNavController.navigate(Route.LifehackDetail(lifehackId))
					},
					onLogout = onLogout
				)
			}
		}
	}
}