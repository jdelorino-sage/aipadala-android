package com.aipadala.android.presentation.screens.landing

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aipadala.android.presentation.components.landing.BottomCTASection
import com.aipadala.android.presentation.components.landing.ComparisonTableSection
import com.aipadala.android.presentation.components.landing.FooterSection
import com.aipadala.android.presentation.components.landing.HeroSection
import com.aipadala.android.presentation.components.landing.HowItWorksSection
import com.aipadala.android.presentation.components.landing.LandingNavBar
import com.aipadala.android.presentation.components.landing.TestimonialsSection
import com.aipadala.android.presentation.components.landing.WhyTrustUsSection
import kotlinx.coroutines.launch

@Composable
fun LandingScreen(
    viewModel: LandingViewModel = hiltViewModel(),
    onNavigateToCompare: () -> Unit,
    onNavigateToCompareWithCurrency: (String) -> Unit,
    onNavigateToSignIn: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToProvider: (String) -> Unit,
    onOpenAffiliateLink: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Handle error messages
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onEvent(LandingEvent.DismissError)
        }
    }

    Scaffold(
        topBar = {
            LandingNavBar(
                currentLanguage = uiState.currentLanguage,
                isMobileMenuOpen = uiState.isMobileMenuOpen,
                onToggleLanguage = {
                    viewModel.onEvent(LandingEvent.ToggleLanguage)
                },
                onToggleMobileMenu = {
                    viewModel.onEvent(LandingEvent.ToggleMobileMenu)
                },
                onNavigateToCompare = onNavigateToCompare,
                onNavigateToHowItWorks = {
                    // Scroll to How It Works section
                    scope.launch {
                        listState.animateScrollToItem(3) // Index of How It Works section
                    }
                },
                onNavigateToAbout = {
                    // Scroll to footer or about section
                    scope.launch {
                        listState.animateScrollToItem(6) // Index of footer
                    }
                },
                onSignInClick = onNavigateToSignIn,
                onGetStartedClick = onNavigateToOnboarding
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = paddingValues
        ) {
            // Section 2: Hero Section with Quick Compare Widget
            item {
                HeroSection(
                    stats = uiState.stats,
                    sendAmount = uiState.sendAmount,
                    selectedCurrency = uiState.selectedCurrency,
                    isComparing = uiState.isComparing,
                    compareResults = uiState.compareResults,
                    showCompareResults = uiState.showCompareResults,
                    onAmountChange = { amount ->
                        viewModel.onEvent(LandingEvent.UpdateSendAmount(amount))
                    },
                    onCurrencySelect = { currency ->
                        viewModel.onEvent(LandingEvent.SelectCurrency(currency))
                    },
                    onCompareClick = {
                        viewModel.onEvent(LandingEvent.Compare)
                    },
                    onProviderClick = { result ->
                        viewModel.onEvent(
                            LandingEvent.TrackProviderClick(
                                providerId = result.providerId,
                                providerName = result.providerName,
                                amount = uiState.sendAmount,
                                currency = uiState.selectedCurrency.code
                            )
                        )
                        result.affiliateUrl?.let { onOpenAffiliateLink(it) }
                    }
                )
            }

            // Section 3: OFW Stories / Testimonials with Stats Bar
            item {
                TestimonialsSection(
                    testimonials = uiState.testimonials,
                    stats = uiState.stats
                )
            }

            // Section 4: Comparison Table
            item {
                ComparisonTableSection(
                    comparisons = uiState.featureComparisons
                )
            }

            // Section 5: How It Works
            item {
                HowItWorksSection()
            }

            // Section 6: Why Trust Us
            item {
                WhyTrustUsSection()
            }

            // Section 7: Bottom CTA
            item {
                BottomCTASection(
                    onGetStartedClick = onNavigateToOnboarding
                )
            }

            // Section 8: Footer
            item {
                FooterSection(
                    onCompareClick = { currencyCode ->
                        onNavigateToCompareWithCurrency(currencyCode)
                    },
                    onLearnClick = { route ->
                        // Handle learn section navigation
                        // This could open a web view or navigate to in-app content
                    },
                    onCompanyClick = { route ->
                        // Handle company section navigation
                    },
                    onSocialClick = { platform ->
                        // Handle social link clicks
                        // This would typically open the social media app or browser
                    },
                    onPrivacyClick = {
                        // Navigate to privacy policy
                    },
                    onTermsClick = {
                        // Navigate to terms of service
                    }
                )
            }
        }
    }
}
