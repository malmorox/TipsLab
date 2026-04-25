package app.iesjdlc.tipslab.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.iesjdlc.tipslab.ui.components.BottomNavBar
import app.iesjdlc.tipslab.ui.screens.explore.ExploreTab
import app.iesjdlc.tipslab.ui.screens.home.HomeTab
import app.iesjdlc.tipslab.ui.screens.profile.ProfileTab
import app.iesjdlc.tipslab.ui.screens.saved.SavedTab

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
				onCreateClick = {
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
					onLifehackClick = { lifehackId ->
						rootNavController.navigate(Route.LifehackDetail(lifehackId))
					},
					onOpenCategory = { categoryId ->
						rootNavController.navigate(Route.LifehacksByCategory(categoryId))
					},
					onOpenSearch = {
						rootNavController.navigate(Route.Explore)
					}
				)
			}

			composable<Route.Explore> {
				ExploreTab(
					onCategoryClick = { categoryId ->
						rootNavController.navigate(Route.LifehacksByCategory(categoryId))
					}
				)
			}

			composable<Route.Saved> {
				SavedTab(
					onLifehackClick = { lifehackId ->
						rootNavController.navigate(Route.LifehackDetail(lifehackId))
					}
				)
			}

			composable<Route.Profile> {
				ProfileTab(
					onEditProfile = {
						rootNavController.navigate(Route.EditProfile)
					},
					onLogout = onLogout
				)
			}
		}
	}
}