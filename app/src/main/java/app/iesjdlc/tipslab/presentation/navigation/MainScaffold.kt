package app.iesjdlc.tipslab.presentation.navigation

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
import app.iesjdlc.tipslab.presentation.screens.saved.SavedTab

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
			composable<Route.Home> {
				HomeTab(
					onOpenLifehack = { lifehackId ->
						rootNavController.navigate(Route.LifehackDetail(lifehackId))
					},
					onOpenCategory = { categoryId ->
						rootNavController.navigate(Route.LifehacksByCategory(categoryId))
					},
					onSearch = {
						rootNavController.navigate(Route.Search)
					}
				)
			}

			composable<Route.Explore> {
				ExploreTab(
					onOpenCategory = { categoryId ->
						rootNavController.navigate(Route.LifehacksByCategory(categoryId))
					},
					onSearch = {
						rootNavController.navigate(Route.Search)
					}
				)
			}

			composable<Route.Saved> {
				SavedTab(

				)
			}

			composable<Route.Profile> {
				ProfileTab(
					onEditProfile = {
						rootNavController.navigate(Route.EditProfile)
					},
					onLogout = onLogout,
					onOpenLifehack = { lifehackId ->
						rootNavController.navigate(Route.LifehackDetail(lifehackId))
					}
				)
			}
		}
	}
}