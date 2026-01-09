package com.aipadala.android.presentation.screens.landing

import com.aipadala.android.data.model.SupportedCurrencies
import com.aipadala.android.domain.usecase.comparison.GetComparisonUseCase
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LandingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: LandingViewModel
    private lateinit var getComparisonUseCase: GetComparisonUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getComparisonUseCase = mockk(relaxed = true)
        viewModel = LandingViewModel(getComparisonUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ══════════════════════════════════════════════════════════════════
    // Initial State Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun `initial state has default values`() {
        val state = viewModel.uiState.value

        assertEquals("en", state.currentLanguage)
        assertEquals(500.0, state.sendAmount, 0.01)
        assertEquals("AUD", state.selectedCurrency.code)
        assertFalse(state.isComparing)
        assertTrue(state.compareResults.isEmpty())
        assertFalse(state.showCompareResults)
        assertFalse(state.isMobileMenuOpen)
        assertEquals(null, state.errorMessage)
    }

    @Test
    fun `initial state has testimonials loaded`() {
        val state = viewModel.uiState.value

        assertTrue(state.testimonials.isNotEmpty())
        assertEquals(5, state.testimonials.size)
    }

    @Test
    fun `initial state has feature comparisons loaded`() {
        val state = viewModel.uiState.value

        assertTrue(state.featureComparisons.isNotEmpty())
        assertEquals(8, state.featureComparisons.size)
    }

    @Test
    fun `initial state has trust pillars loaded`() {
        val state = viewModel.uiState.value

        assertTrue(state.trustPillars.isNotEmpty())
        assertEquals(4, state.trustPillars.size)
    }

    @Test
    fun `initial state has stats loaded`() {
        val state = viewModel.uiState.value

        assertNotNull(state.stats)
        assertEquals("10,000+", state.stats.ofwFamilies)
        assertEquals("₱25M+", state.stats.totalSaved)
        assertEquals("4.9", state.stats.averageRating)
        assertEquals("50+", state.stats.countries)
    }

    // ══════════════════════════════════════════════════════════════════
    // Amount Update Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun `updateSendAmount updates amount correctly`() {
        viewModel.onEvent(LandingEvent.UpdateSendAmount(1000.0))

        assertEquals(1000.0, viewModel.uiState.value.sendAmount, 0.01)
    }

    @Test
    fun `updateSendAmount coerces minimum value`() {
        viewModel.onEvent(LandingEvent.UpdateSendAmount(0.0))

        assertEquals(1.0, viewModel.uiState.value.sendAmount, 0.01)
    }

    @Test
    fun `updateSendAmount coerces maximum value`() {
        viewModel.onEvent(LandingEvent.UpdateSendAmount(200000.0))

        assertEquals(100000.0, viewModel.uiState.value.sendAmount, 0.01)
    }

    @Test
    fun `updateSendAmount handles negative values`() {
        viewModel.onEvent(LandingEvent.UpdateSendAmount(-100.0))

        assertEquals(1.0, viewModel.uiState.value.sendAmount, 0.01)
    }

    // ══════════════════════════════════════════════════════════════════
    // Currency Selection Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun `selectCurrency updates selected currency`() {
        val usdCurrency = SupportedCurrencies.SOURCE_CURRENCIES.first { it.code == "USD" }

        viewModel.onEvent(LandingEvent.SelectCurrency(usdCurrency))

        assertEquals("USD", viewModel.uiState.value.selectedCurrency.code)
    }

    @Test
    fun `selectCurrency clears previous results`() = runTest {
        // First perform a comparison
        viewModel.onEvent(LandingEvent.Compare)
        advanceUntilIdle()

        // Then select a new currency
        val sgdCurrency = SupportedCurrencies.SOURCE_CURRENCIES.first { it.code == "SGD" }
        viewModel.onEvent(LandingEvent.SelectCurrency(sgdCurrency))

        assertFalse(viewModel.uiState.value.showCompareResults)
        assertTrue(viewModel.uiState.value.compareResults.isEmpty())
    }

    // ══════════════════════════════════════════════════════════════════
    // Comparison Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun `compare sets isComparing to true during loading`() = runTest {
        viewModel.onEvent(LandingEvent.Compare)

        assertTrue(viewModel.uiState.value.isComparing)
    }

    @Test
    fun `compare returns results after completion`() = runTest {
        viewModel.onEvent(LandingEvent.Compare)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isComparing)
        assertTrue(viewModel.uiState.value.showCompareResults)
        assertTrue(viewModel.uiState.value.compareResults.isNotEmpty())
    }

    @Test
    fun `compare results are sorted by recipient gets descending`() = runTest {
        viewModel.onEvent(LandingEvent.Compare)
        advanceUntilIdle()

        val results = viewModel.uiState.value.compareResults
        for (i in 0 until results.size - 1) {
            assertTrue(results[i].recipientGets >= results[i + 1].recipientGets)
        }
    }

    @Test
    fun `compare marks best rate correctly`() = runTest {
        viewModel.onEvent(LandingEvent.Compare)
        advanceUntilIdle()

        val results = viewModel.uiState.value.compareResults
        assertTrue(results.first().isBestRate)
        assertTrue(results.drop(1).none { it.isBestRate })
    }

    @Test
    fun `clearResults clears compare results`() = runTest {
        viewModel.onEvent(LandingEvent.Compare)
        advanceUntilIdle()

        viewModel.onEvent(LandingEvent.ClearResults)

        assertFalse(viewModel.uiState.value.showCompareResults)
        assertTrue(viewModel.uiState.value.compareResults.isEmpty())
    }

    // ══════════════════════════════════════════════════════════════════
    // Language Toggle Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun `toggleLanguage switches from en to fil`() {
        viewModel.onEvent(LandingEvent.ToggleLanguage)

        assertEquals("fil", viewModel.uiState.value.currentLanguage)
    }

    @Test
    fun `toggleLanguage switches from fil to en`() {
        viewModel.onEvent(LandingEvent.ToggleLanguage)
        viewModel.onEvent(LandingEvent.ToggleLanguage)

        assertEquals("en", viewModel.uiState.value.currentLanguage)
    }

    // ══════════════════════════════════════════════════════════════════
    // Mobile Menu Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun `toggleMobileMenu opens menu`() {
        viewModel.onEvent(LandingEvent.ToggleMobileMenu)

        assertTrue(viewModel.uiState.value.isMobileMenuOpen)
    }

    @Test
    fun `toggleMobileMenu closes menu when already open`() {
        viewModel.onEvent(LandingEvent.ToggleMobileMenu)
        viewModel.onEvent(LandingEvent.ToggleMobileMenu)

        assertFalse(viewModel.uiState.value.isMobileMenuOpen)
    }

    // ══════════════════════════════════════════════════════════════════
    // Error Handling Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun `dismissError clears error message`() = runTest {
        // Set an error state manually is not possible, so we test the dismiss action
        viewModel.onEvent(LandingEvent.DismissError)

        assertEquals(null, viewModel.uiState.value.errorMessage)
    }

    // ══════════════════════════════════════════════════════════════════
    // Provider Click Tracking Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun `trackProviderClick does not crash`() = runTest {
        viewModel.onEvent(
            LandingEvent.TrackProviderClick(
                providerId = "wise",
                providerName = "Wise",
                amount = 500.0,
                currency = "AUD"
            )
        )
        advanceUntilIdle()

        // Test passes if no exception is thrown
        assertTrue(true)
    }

    // ══════════════════════════════════════════════════════════════════
    // Exchange Rate Calculation Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun `comparison uses correct base rate for AUD`() = runTest {
        viewModel.onEvent(LandingEvent.Compare)
        advanceUntilIdle()

        val results = viewModel.uiState.value.compareResults
        assertTrue(results.isNotEmpty())
        // AUD base rate is 36.52, with variance it should be close
        val avgRate = results.map { it.exchangeRate }.average()
        assertTrue(avgRate > 35 && avgRate < 38)
    }

    @Test
    fun `comparison calculates recipient gets correctly`() = runTest {
        val testAmount = 500.0
        viewModel.onEvent(LandingEvent.UpdateSendAmount(testAmount))
        viewModel.onEvent(LandingEvent.Compare)
        advanceUntilIdle()

        val results = viewModel.uiState.value.compareResults
        results.forEach { result ->
            val expectedRecipientGets = (testAmount - result.transferFee) * result.exchangeRate
            assertEquals(expectedRecipientGets, result.recipientGets, 0.01)
        }
    }

    // ══════════════════════════════════════════════════════════════════
    // Testimonials Data Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun `testimonials have all required fields`() {
        val testimonials = viewModel.uiState.value.testimonials

        testimonials.forEach { testimonial ->
            assertTrue(testimonial.id.isNotEmpty())
            assertTrue(testimonial.name.isNotEmpty())
            assertTrue(testimonial.countryFlag.isNotEmpty())
            assertTrue(testimonial.countryName.isNotEmpty())
            assertTrue(testimonial.jobTitle.isNotEmpty())
            assertTrue(testimonial.quote.isNotEmpty())
            assertTrue(testimonial.monthlySavings.isNotEmpty())
        }
    }

    @Test
    fun `testimonials represent diverse countries`() {
        val testimonials = viewModel.uiState.value.testimonials
        val countries = testimonials.map { it.countryName }.toSet()

        assertTrue(countries.size >= 4)
        assertTrue(countries.contains("UAE"))
        assertTrue(countries.contains("Singapore"))
    }

    // ══════════════════════════════════════════════════════════════════
    // Feature Comparison Data Tests
    // ══════════════════════════════════════════════════════════════════

    @Test
    fun `feature comparisons favor AI Padala`() {
        val comparisons = viewModel.uiState.value.featureComparisons

        val aiPadalaWins = comparisons.count { it.aiPadalaHasIt && !it.othersHasIt }
        assertTrue(aiPadalaWins >= comparisons.size / 2)
    }

    @Test
    fun `feature comparisons include key features`() {
        val comparisons = viewModel.uiState.value.featureComparisons
        val features = comparisons.map { it.feature }

        assertTrue(features.any { it.contains("Fee", ignoreCase = true) })
        assertTrue(features.any { it.contains("Rate", ignoreCase = true) })
        assertTrue(features.any { it.contains("Speed", ignoreCase = true) })
    }
}
