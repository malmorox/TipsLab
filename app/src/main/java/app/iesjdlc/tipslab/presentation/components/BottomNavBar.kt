package app.iesjdlc.tipslab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import app.iesjdlc.tipslab.presentation.navigation.Route

private sealed class BottomNavItem(
    val label: String,
    val icon: ImageVector
) {
    // Items normales que navegan por el innerNavController
    data object Home : BottomNavItem("Home", Icons.Rounded.Home)
    data object Explore : BottomNavItem("Explore", Icons.Rounded.Search)
    data object Create : BottomNavItem("Create", Icons.Rounded.Add)
    data object Saved : BottomNavItem("Saved", Icons.Rounded.Search)
    data object Profile : BottomNavItem("Profile", Icons.Rounded.Person)

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
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label
                        )
                    },
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