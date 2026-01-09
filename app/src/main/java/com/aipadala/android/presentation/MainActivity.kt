package com.aipadala.android.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aipadala.android.core.util.AffiliateLinkHandler
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

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AIPadalaTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

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
                        onAffiliateClick = { url ->
                            // Extract provider from URL or use a default
                            val provider = RemittanceProvider.entries.find {
                                url.contains(it.name.lowercase())
                            } ?: RemittanceProvider.WISE

                            affiliateLinkHandler.openAffiliateLink(
                                provider = provider,
                                url = url,
                                corridor = "PHP",
                                amount = 500.0
                            )
                        }
                    )
                }
            }
        }
    }
}
