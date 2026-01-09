package com.aipadala.android.presentation.navigation

sealed class Screen(val route: String) {
    // Bottom Navigation Destinations
    data object Home : Screen("home")
    data object Compare : Screen("compare")
    data object Alerts : Screen("alerts")
    data object Favorites : Screen("favorites")
    data object Settings : Screen("settings")

    // Nested Screens
    data object ComparisonResult : Screen("compare/result/{fromCurrency}/{toCurrency}/{amount}") {
        fun createRoute(from: String, to: String, amount: Double) =
            "compare/result/$from/$to/$amount"
    }

    data object ProviderDetail : Screen("provider/{providerId}") {
        fun createRoute(providerId: String) = "provider/$providerId"
    }

    data object AlertCreate : Screen("alerts/create")

    data object AlertEdit : Screen("alerts/edit/{alertId}") {
        fun createRoute(alertId: String) = "alerts/edit/$alertId"
    }

    data object Onboarding : Screen("onboarding")
}
