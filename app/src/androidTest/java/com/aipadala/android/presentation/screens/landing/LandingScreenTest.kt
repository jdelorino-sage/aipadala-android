package com.aipadala.android.presentation.screens.landing

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aipadala.android.data.model.DefaultFeatureComparisons
import com.aipadala.android.data.model.DefaultTestimonials
import com.aipadala.android.data.model.LandingStats
import com.aipadala.android.data.model.SupportedCurrencies
import com.aipadala.android.presentation.components.landing.ComparisonTableSection
import com.aipadala.android.presentation.components.landing.HeroSection
import com.aipadala.android.presentation.components.landing.HowItWorksSection
import com.aipadala.android.presentation.components.landing.TestimonialsSection
import com.aipadala.android.presentation.components.landing.WhyTrustUsSection
import com.aipadala.android.presentation.theme.AIPadalaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LandingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ══════════════════════════════════════════════════════════════════
    // Hero Section Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun heroSection_displaysHeadline() {
        composeTestRule.setContent {
            AIPadalaTheme {
                HeroSection(
                    stats = LandingStats(),
                    sendAmount = 500.0,
                    selectedCurrency = SupportedCurrencies.SOURCE_CURRENCIES.first { it.code == "AUD" },
                    isComparing = false,
                    compareResults = emptyList(),
                    showCompareResults = false,
                    onAmountChange = {},
                    onCurrencySelect = {},
                    onCompareClick = {},
                    onProviderClick = {}
                )
            }
        }

        // Check that the Tagalog headline is displayed
        composeTestRule.onNodeWithText("Malayo ka man, nararamdaman ka nila sa bawat padala.", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun heroSection_displaysTrustBadge() {
        composeTestRule.setContent {
            AIPadalaTheme {
                HeroSection(
                    stats = LandingStats(),
                    sendAmount = 500.0,
                    selectedCurrency = SupportedCurrencies.SOURCE_CURRENCIES.first { it.code == "AUD" },
                    isComparing = false,
                    compareResults = emptyList(),
                    showCompareResults = false,
                    onAmountChange = {},
                    onCurrencySelect = {},
                    onCompareClick = {},
                    onProviderClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("10,000+", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun heroSection_displaysQuickCompareWidget() {
        composeTestRule.setContent {
            AIPadalaTheme {
                HeroSection(
                    stats = LandingStats(),
                    sendAmount = 500.0,
                    selectedCurrency = SupportedCurrencies.SOURCE_CURRENCIES.first { it.code == "AUD" },
                    isComparing = false,
                    compareResults = emptyList(),
                    showCompareResults = false,
                    onAmountChange = {},
                    onCurrencySelect = {},
                    onCompareClick = {},
                    onProviderClick = {}
                )
            }
        }

        // Check that AUD currency is displayed
        composeTestRule.onNodeWithText("AUD")
            .assertIsDisplayed()
    }

    @Test
    fun heroSection_displaysSocialProofStats() {
        composeTestRule.setContent {
            AIPadalaTheme {
                HeroSection(
                    stats = LandingStats(
                        totalSaved = "₱25M+",
                        ofwFamilies = "10,000+"
                    ),
                    sendAmount = 500.0,
                    selectedCurrency = SupportedCurrencies.SOURCE_CURRENCIES.first { it.code == "AUD" },
                    isComparing = false,
                    compareResults = emptyList(),
                    showCompareResults = false,
                    onAmountChange = {},
                    onCurrencySelect = {},
                    onCompareClick = {},
                    onProviderClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("₱25M+")
            .assertIsDisplayed()
    }

    // ══════════════════════════════════════════════════════════════════
    // Testimonials Section Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun testimonialsSection_displaysSectionTitle() {
        composeTestRule.setContent {
            AIPadalaTheme {
                TestimonialsSection(
                    testimonials = DefaultTestimonials.testimonials,
                    stats = LandingStats()
                )
            }
        }

        composeTestRule.onNodeWithText("Voices from Our OFW Community", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun testimonialsSection_displaysTestimonialCards() {
        composeTestRule.setContent {
            AIPadalaTheme {
                TestimonialsSection(
                    testimonials = DefaultTestimonials.testimonials,
                    stats = LandingStats()
                )
            }
        }

        // Check that first testimonial name is displayed
        composeTestRule.onNodeWithText("Maria Santos")
            .assertIsDisplayed()
    }

    @Test
    fun testimonialsSection_displaysStatsBar() {
        composeTestRule.setContent {
            AIPadalaTheme {
                TestimonialsSection(
                    testimonials = DefaultTestimonials.testimonials,
                    stats = LandingStats(
                        ofwFamilies = "10,000+",
                        totalSaved = "₱25M+",
                        averageRating = "4.9",
                        countries = "50+"
                    )
                )
            }
        }

        composeTestRule.onNodeWithText("4.9")
            .assertIsDisplayed()
    }

    // ══════════════════════════════════════════════════════════════════
    // Comparison Table Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun comparisonTable_displaysSectionTitle() {
        composeTestRule.setContent {
            AIPadalaTheme {
                ComparisonTableSection(
                    comparisons = DefaultFeatureComparisons.comparisons
                )
            }
        }

        composeTestRule.onNodeWithText("AI Padala vs Traditional Apps", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun comparisonTable_displaysFeatures() {
        composeTestRule.setContent {
            AIPadalaTheme {
                ComparisonTableSection(
                    comparisons = DefaultFeatureComparisons.comparisons
                )
            }
        }

        // Check that Transfer Fee feature is displayed
        composeTestRule.onNodeWithText("Transfer Fee")
            .assertIsDisplayed()
    }

    @Test
    fun comparisonTable_displaysAIPadalaHeader() {
        composeTestRule.setContent {
            AIPadalaTheme {
                ComparisonTableSection(
                    comparisons = DefaultFeatureComparisons.comparisons
                )
            }
        }

        composeTestRule.onNodeWithText("AI Padala")
            .assertIsDisplayed()
    }

    // ══════════════════════════════════════════════════════════════════
    // How It Works Section Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun howItWorksSection_displaysSectionTitle() {
        composeTestRule.setContent {
            AIPadalaTheme {
                HowItWorksSection()
            }
        }

        composeTestRule.onNodeWithText("How It Works")
            .assertIsDisplayed()
    }

    @Test
    fun howItWorksSection_displaysAllSteps() {
        composeTestRule.setContent {
            AIPadalaTheme {
                HowItWorksSection()
            }
        }

        // Check all 3 steps are displayed
        composeTestRule.onNodeWithText("Sign Up")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Enter Amount")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Send!")
            .assertIsDisplayed()
    }

    // ══════════════════════════════════════════════════════════════════
    // Why Trust Us Section Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun whyTrustUsSection_displaysSectionTitle() {
        composeTestRule.setContent {
            AIPadalaTheme {
                WhyTrustUsSection()
            }
        }

        composeTestRule.onNodeWithText("Why Trust Us")
            .assertIsDisplayed()
    }

    @Test
    fun whyTrustUsSection_displaysTrustPillars() {
        composeTestRule.setContent {
            AIPadalaTheme {
                WhyTrustUsSection()
            }
        }

        // Check all 4 trust pillars are displayed
        composeTestRule.onNodeWithText("Bank-Level Security")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Instant Transfers")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Best Rates Guaranteed")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("24/7 Tagalog Support")
            .assertIsDisplayed()
    }
}
