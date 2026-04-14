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
			startDestination = Route.HomeTab,
			modifier = Modifier.padding(innerPadding)
		) {
			composable<Route.HomeTab> {
				HomeTab(
					onLifehackClick = { id ->
						rootNavController.navigate(Route.LifehackDetail(id))
					}
				)
			}

			composable<Route.ExploreTab> {
				ExploreTab(
					onLifehackClick = { id ->
						rootNavController.navigate(Route.LifehackDetail(id))
					},
					onCategoryClick = { id ->
						rootNavController.navigate(Route.LifehacksByCategory(id))
					}
				)
			}

			composable<Route.SavedTab> {
				SavedTab(
					onLifehackClick = { id ->
						rootNavController.navigate(Route.LifehackDetail(id))
					}
				)
			}

			composable<Route.ProfileTab> {
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