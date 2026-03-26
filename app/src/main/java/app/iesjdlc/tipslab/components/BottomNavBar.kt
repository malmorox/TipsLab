package app.iesjdlc.tipslab.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import app.iesjdlc.tipslab.navigation.Route

private data class BottomNavItem(
    val route: Route,
    val label: String,
    val icon: ImageVector
)

@Composable
fun BottomNavBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val bottomNavItems = listOf(
        BottomNavItem(
            route = Route.HomeTab,
            label = "Home",
            icon = Icons.Sharp.Home
        ),
        BottomNavItem(
            route = Route.ExploreTab,
            label = "Explore",
            icon = Icons.Sharp.Search
        ),
        BottomNavItem(
            route = Route.CreateTab,
            label = "Create",
            icon = Icons.Sharp.Add
        ),
        BottomNavItem(
            route = Route.SavedTab,
            label = "Saved",
            icon = Icons.Sharp.Home
        ),
        BottomNavItem(
            route = Route.ProfileTab,
            label = "Profile",
            icon = Icons.Sharp.Home
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavigationBar {
            bottomNavItems.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = currentRoute == item.route::class.qualifiedName,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}