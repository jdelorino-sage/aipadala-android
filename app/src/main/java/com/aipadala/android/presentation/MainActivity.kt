package com.aipadala.android.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aipadala.android.core.util.AffiliateLinkHandler
import com.aipadala.android.core.util.Constants
import com.aipadala.android.data.model.RemittanceProvider
import com.aipadala.android.presentation.components.common.AIPadalaBottomNav
import com.aipadala.android.presentation.navigation.AIPadalaNavHost
import com.aipadala.android.presentation.navigation.Screen
import com.aipadala.android.presentation.theme.AIPadalaTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var affiliateLinkHandler: AffiliateLinkHandler

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Permission result handled - notifications will work if granted
        // No action needed if denied, the app will continue to function
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Request notification permission on Android 13+
        requestNotificationPermission()

        setContent {
            AIPadalaTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Handle deep links from intent
                var deepLinkHandled by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    if (!deepLinkHandled) {
                        handleDeepLink(intent, navController)
                        deepLinkHandled = true
                    }
                }

                // Routes that should show bottom nav
                val showBottomNav = currentRoute in listOf(
                    Screen.Home.route,
                    Screen.Compare.route,
                    Screen.Alerts.route,
                    Screen.Favorites.route,
                    Screen.Settings.route
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomNav) {
                            AIPadalaBottomNav(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    AIPadalaNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        onAffiliateClick = { url, fromCurrency, toCurrency, amount ->
                            // Extract provider from URL or use a default
                            val provider = RemittanceProvider.entries.find {
                                url.contains(it.name.lowercase())
                            } ?: RemittanceProvider.WISE

                            affiliateLinkHandler.openAffiliateLink(
                                provider = provider,
                                url = url,
                                corridor = "$fromCurrency→$toCurrency",
                                amount = amount
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Deep link will be handled on next recomposition
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    // Show rationale if needed, then request
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                else -> {
                    // Request permission directly
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun handleDeepLink(intent: Intent?, navController: NavHostController) {
        intent?.data?.let { uri ->
            val path = uri.path ?: return@let

            when {
                // Handle compare deep link: aipadala.com/compare?from=USD&amount=500
                path.startsWith("/${Constants.DEEP_LINK_COMPARE}") -> {
                    val fromCurrency = uri.getQueryParameter("from") ?: "USD"
                    val amount = uri.getQueryParameter("amount")?.toDoubleOrNull()
                        ?: Constants.DEFAULT_SEND_AMOUNT
                    navController.navigate(
                        Screen.ComparisonResult.createRoute(fromCurrency, "PHP", amount)
                    )
                }

                // Handle alerts deep link: aipadala.com/alerts
                path.startsWith("/${Constants.DEEP_LINK_ALERTS}") -> {
                    navController.navigate(Screen.Alerts.route)
                }

                // Handle provider deep link: aipadala.com/provider/wise
                path.startsWith("/${Constants.DEEP_LINK_PROVIDER}/") -> {
                    val providerId = path.substringAfterLast("/")
                    if (providerId.isNotBlank()) {
                        navController.navigate(Screen.ProviderDetail.createRoute(providerId))
                    }
                }
            }
        }

        // Handle notification extras
        intent?.extras?.let { extras ->
            val corridor = extras.getString("corridor")
            if (!corridor.isNullOrBlank() && corridor.contains("→")) {
                val (from, to) = corridor.split("→")
                val rate = extras.getDouble("rate", Constants.DEFAULT_SEND_AMOUNT)
                navController.navigate(Screen.ComparisonResult.createRoute(from, to, rate))
            }
        }
    }
}
