package app.iesjdlc.tipslab.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.iesjdlc.tipslab.components.BottomNavBar
import app.iesjdlc.tipslab.screens.CreateLifehackTab
import app.iesjdlc.tipslab.screens.ExploreTab
import app.iesjdlc.tipslab.screens.HomeTab
import app.iesjdlc.tipslab.screens.SavedTab

@Composable
fun MainScaffold(onLogout: () -> Unit) {
	val innerNavController = rememberNavController()

	Scaffold(
		bottomBar = { BottomNavBar(navController = innerNavController) }
	) { innerPadding ->
		NavHost(
			navController = innerNavController,
			startDestination = Route.HomeTab,
			modifier = Modifier.padding(innerPadding)
		) {
			composable<Route.HomeTab> { HomeTab() }
			composable<Route.ExploreTab> { ExploreTab() }
			composable<Route.CreateTab> { CreateLifehackTab() }
			composable<Route.SavedTab> { SavedTab() }
		}
	}
}