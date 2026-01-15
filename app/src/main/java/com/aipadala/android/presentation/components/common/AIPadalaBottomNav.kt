package com.aipadala.android.presentation.components.common

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.aipadala.android.R
import com.aipadala.android.presentation.navigation.Screen
import com.aipadala.android.presentation.theme.AIPadalaColors
import com.aipadala.android.presentation.theme.Dimensions

data class BottomNavItem(
    val route: String,
    val titleRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = Screen.Home.route,
        titleRes = R.string.nav_home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItem(
        route = Screen.Compare.route,
        titleRes = R.string.nav_compare,
        selectedIcon = Icons.Filled.CompareArrows,
        unselectedIcon = Icons.Outlined.CompareArrows
    ),
    BottomNavItem(
        route = Screen.Alerts.route,
        titleRes = R.string.nav_alerts,
        selectedIcon = Icons.Filled.Notifications,
        unselectedIcon = Icons.Outlined.Notifications
    ),
    BottomNavItem(
        route = Screen.Favorites.route,
        titleRes = R.string.nav_favorites,
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    ),
    BottomNavItem(
        route = Screen.Settings.route,
        titleRes = R.string.nav_settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
)

/**
 * Apple iOS-inspired bottom navigation (Tab Bar)
 * Clean, minimal with subtle selection state
 */
@Composable
fun AIPadalaBottomNav(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier.height(Dimensions.BottomNavHeight),
        containerColor = AIPadalaColors.White,
        contentColor = AIPadalaColors.Gray500,
        tonalElevation = 0.dp  // No elevation - Apple style uses border instead
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = stringResource(item.titleRes),
                        modifier = Modifier.height(22.dp)  // iOS tab bar icon size
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.titleRes),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,  // iOS tab bar label size
                            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AIPadalaColors.Primary500,
                    selectedTextColor = AIPadalaColors.Primary500,
                    unselectedIconColor = AIPadalaColors.Gray400,
                    unselectedTextColor = AIPadalaColors.Gray400,
                    indicatorColor = Color.Transparent  // No indicator - Apple style
                )
            )
        }
    }
}
