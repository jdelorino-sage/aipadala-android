package com.aipadala.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aipadala.android.presentation.screens.alerts.AlertsScreen
import com.aipadala.android.presentation.screens.alerts.CreateAlertScreen
import com.aipadala.android.presentation.screens.comparison.ComparisonResultScreen
import com.aipadala.android.presentation.screens.comparison.ComparisonScreen
import com.aipadala.android.presentation.screens.favorites.FavoritesScreen
import com.aipadala.android.presentation.screens.home.HomeScreen
import com.aipadala.android.presentation.screens.onboarding.OnboardingScreen
import com.aipadala.android.presentation.screens.provider.ProviderDetailScreen
import com.aipadala.android.presentation.screens.settings.SettingsScreen

@Composable
fun AIPadalaNavHost(
    navController: NavHostController,
    startDestination: String = Screen.Home.route,
    modifier: Modifier = Modifier,
    onAffiliateClick: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Home
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCompare = {
                    navController.navigate(Screen.Compare.route)
                },
                onNavigateToAlerts = {
                    navController.navigate(Screen.Alerts.route)
                },
                onNavigateToProvider = { providerId ->
                    navController.navigate(Screen.ProviderDetail.createRoute(providerId))
                }
            )
        }

        // Compare
        composable(Screen.Compare.route) {
            ComparisonScreen(
                onNavigateToResult = { from, to, amount ->
                    navController.navigate(Screen.ComparisonResult.createRoute(from, to, amount))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Comparison Results
        composable(
            route = Screen.ComparisonResult.route,
            arguments = listOf(
                navArgument("fromCurrency") { type = NavType.StringType },
                navArgument("toCurrency") { type = NavType.StringType },
                navArgument("amount") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val fromCurrency = backStackEntry.arguments?.getString("fromCurrency") ?: ""
            val toCurrency = backStackEntry.arguments?.getString("toCurrency") ?: "PHP"
            val amount = backStackEntry.arguments?.getFloat("amount")?.toDouble() ?: 0.0

            ComparisonResultScreen(
                fromCurrency = fromCurrency,
                toCurrency = toCurrency,
                amount = amount,
                onNavigateToProvider = { providerId ->
                    navController.navigate(Screen.ProviderDetail.createRoute(providerId))
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onOpenAffiliate = onAffiliateClick
            )
        }

        // Provider Detail
        composable(
            route = Screen.ProviderDetail.route,
            arguments = listOf(
                navArgument("providerId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val providerId = backStackEntry.arguments?.getString("providerId") ?: ""
            ProviderDetailScreen(
                providerId = providerId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onOpenAffiliate = onAffiliateClick
            )
        }

        // Alerts
        composable(Screen.Alerts.route) {
            AlertsScreen(
                onNavigateToCreate = {
                    navController.navigate(Screen.AlertCreate.route)
                },
                onNavigateToEdit = { alertId ->
                    navController.navigate(Screen.AlertEdit.createRoute(alertId))
                }
            )
        }

        // Create Alert
        composable(Screen.AlertCreate.route) {
            CreateAlertScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAlertCreated = {
                    navController.popBackStack()
                }
            )
        }

        // Favorites
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onNavigateToCompare = { from, to, amount ->
                    navController.navigate(Screen.ComparisonResult.createRoute(from, to, amount))
                }
            )
        }

        // Settings
        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        // Onboarding
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
