package app.iesjdlc.tipslab.ui.components

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
import app.iesjdlc.tipslab.ui.navigation.Route

private sealed class BottomNavItem(
    val label: String,
    val icon: ImageVector
) {
    // Items normales que navegan por el innerNavController
    data object Home : BottomNavItem("Home", Icons.Sharp.Home)
    data object Explore : BottomNavItem("Explore", Icons.Sharp.Search)
    data object Create : BottomNavItem("Create", Icons.Sharp.Add)
    data object Saved : BottomNavItem("Saved", Icons.Sharp.Home)
    data object Profile : BottomNavItem("Profile", Icons.Sharp.Home)

    // La ruta asociada a cada item, menos el de crear
    val route: Route? get() = when (this) {
        Home -> Route.Home
        Explore -> Route.Explore
        Create -> null
        Saved -> Route.Saved
        Profile -> Route.Profile
    }
}

@Composable
fun BottomNavBar(
    navController: NavController,
    onCreate: () -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Explore,
        BottomNavItem.Create,
        BottomNavItem.Saved,
        BottomNavItem.Profile
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavigationBar {
            items.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = currentRoute == item.route?.let { it::class.qualifiedName },
                    onClick = {
                        if (item is BottomNavItem.Create) {
                            onCreate()
                        } else {
                            item.route?.let { route ->
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}