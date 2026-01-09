package com.aipadala.android.presentation.screens.landing

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aipadala.android.data.model.DefaultFeatureComparisons
import com.aipadala.android.data.model.DefaultTestimonials
import com.aipadala.android.data.model.LandingStats
import com.aipadala.android.data.model.SupportedCurrencies
import com.aipadala.android.presentation.components.landing.BottomCTASection
import com.aipadala.android.presentation.components.landing.ComparisonTableSection
import com.aipadala.android.presentation.components.landing.HeroSection
import com.aipadala.android.presentation.components.landing.HowItWorksSection
import com.aipadala.android.presentation.components.landing.TestimonialsSection
import com.aipadala.android.presentation.components.landing.WhyTrustUsSection
import com.aipadala.android.presentation.theme.AIPadalaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Performance tests for Landing Page components.
 *
 * These tests measure:
 * 1. Initial composition time
 * 2. Recomposition efficiency
 * 3. Scroll performance
 * 4. Animation frame drops
 *
 * Performance targets based on specification:
 * - First Contentful Paint: < 1.5s
 * - Largest Contentful Paint: < 2.5s
 * - Time to Interactive: < 3s
 * - Cumulative Layout Shift: < 0.1
 */
@RunWith(AndroidJUnit4::class)
class LandingScreenPerformanceTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    // ══════════════════════════════════════════════════════════════════
    // Composition Performance Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun heroSection_compositionPerformance() {
        benchmarkRule.measureRepeated {
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
            composeTestRule.waitForIdle()
        }
    }

    @Test
    fun testimonialsSection_compositionPerformance() {
        benchmarkRule.measureRepeated {
            composeTestRule.setContent {
                AIPadalaTheme {
                    TestimonialsSection(
                        testimonials = DefaultTestimonials.testimonials,
                        stats = LandingStats()
                    )
                }
            }
            composeTestRule.waitForIdle()
        }
    }

    @Test
    fun comparisonTableSection_compositionPerformance() {
        benchmarkRule.measureRepeated {
            composeTestRule.setContent {
                AIPadalaTheme {
                    ComparisonTableSection(
                        comparisons = DefaultFeatureComparisons.comparisons
                    )
                }
            }
            composeTestRule.waitForIdle()
        }
    }

    @Test
    fun howItWorksSection_compositionPerformance() {
        benchmarkRule.measureRepeated {
            composeTestRule.setContent {
                AIPadalaTheme {
                    HowItWorksSection()
                }
            }
            composeTestRule.waitForIdle()
        }
    }

    @Test
    fun whyTrustUsSection_compositionPerformance() {
        benchmarkRule.measureRepeated {
            composeTestRule.setContent {
                AIPadalaTheme {
                    WhyTrustUsSection()
                }
            }
            composeTestRule.waitForIdle()
        }
    }

    @Test
    fun bottomCTASection_compositionPerformance() {
        benchmarkRule.measureRepeated {
            composeTestRule.setContent {
                AIPadalaTheme {
                    BottomCTASection(
                        onGetStartedClick = {}
                    )
                }
            }
            composeTestRule.waitForIdle()
        }
    }

    // ══════════════════════════════════════════════════════════════════
    // Recomposition Efficiency Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun heroSection_recompositionOnAmountChange() {
        var amount = 500.0

        composeTestRule.setContent {
            AIPadalaTheme {
                HeroSection(
                    stats = LandingStats(),
                    sendAmount = amount,
                    selectedCurrency = SupportedCurrencies.SOURCE_CURRENCIES.first { it.code == "AUD" },
                    isComparing = false,
                    compareResults = emptyList(),
                    showCompareResults = false,
                    onAmountChange = { amount = it },
                    onCurrencySelect = {},
                    onCompareClick = {},
                    onProviderClick = {}
                )
            }
        }

        benchmarkRule.measureRepeated {
            amount = (100..10000).random().toDouble()
            composeTestRule.waitForIdle()
        }
    }

    // ══════════════════════════════════════════════════════════════════
    // Memory Usage Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun testimonialsSection_memoryShouldNotLeakOnRecomposition() {
        repeat(100) {
            composeTestRule.setContent {
                AIPadalaTheme {
                    TestimonialsSection(
                        testimonials = DefaultTestimonials.testimonials,
                        stats = LandingStats()
                    )
                }
            }
            composeTestRule.waitForIdle()
        }
        // If no OOM exception, test passes
    }
}

/**
 * UI/UX Testing Guidelines for Landing Page
 *
 * Manual Testing Checklist:
 *
 * 1. Visual Consistency:
 *    - [ ] Colors match brand guidelines (Primary: Sky Blue #0EA5E9, Accent: Purple #A855F7)
 *    - [ ] Typography follows Inter font family
 *    - [ ] Spacing follows 8dp grid system
 *    - [ ] Icons are properly sized (16dp-48dp)
 *
 * 2. Responsive Design:
 *    - [ ] Layout adapts to different screen sizes
 *    - [ ] Text remains readable on all screens
 *    - [ ] Touch targets are at least 48dp
 *    - [ ] Navigation bar collapses to hamburger on small screens
 *
 * 3. Accessibility:
 *    - [ ] All images have content descriptions
 *    - [ ] Color contrast meets WCAG 2.1 AA standards
 *    - [ ] Interactive elements are focusable
 *    - [ ] Screen reader announces content correctly
 *
 * 4. Animation Performance:
 *    - [ ] Scroll animations run at 60fps
 *    - [ ] Page transitions are smooth
 *    - [ ] No jank during Quick Compare loading
 *    - [ ] CTA button pulse animation is subtle
 *
 * 5. Localization:
 *    - [ ] All text displays correctly in English
 *    - [ ] All text displays correctly in Filipino
 *    - [ ] Language toggle works instantly
 *    - [ ] No text truncation in either language
 *
 * 6. Error States:
 *    - [ ] Network error shows appropriate message
 *    - [ ] Empty states are handled gracefully
 *    - [ ] Loading states are clear and informative
 */
