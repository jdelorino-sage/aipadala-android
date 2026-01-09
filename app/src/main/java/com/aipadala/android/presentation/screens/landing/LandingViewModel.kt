package com.aipadala.android.presentation.screens.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aipadala.android.data.model.Currency
import com.aipadala.android.data.model.DefaultFeatureComparisons
import com.aipadala.android.data.model.DefaultHowItWorksSteps
import com.aipadala.android.data.model.DefaultTestimonials
import com.aipadala.android.data.model.DefaultTrustPillars
import com.aipadala.android.data.model.FeatureComparison
import com.aipadala.android.data.model.HowItWorksStep
import com.aipadala.android.data.model.LandingStats
import com.aipadala.android.data.model.QuickCompareResult
import com.aipadala.android.data.model.SupportedCurrencies
import com.aipadala.android.data.model.Testimonial
import com.aipadala.android.data.model.TrustPillar
import com.aipadala.android.domain.usecase.comparison.GetComparisonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LandingUiState(
    // Language
    val currentLanguage: String = "en",

    // Quick Compare Widget
    val sendAmount: Double = 500.0,
    val selectedCurrency: Currency = SupportedCurrencies.SOURCE_CURRENCIES.first { it.code == "AUD" },
    val isComparing: Boolean = false,
    val compareResults: List<QuickCompareResult> = emptyList(),
    val showCompareResults: Boolean = false,

    // Stats
    val stats: LandingStats = LandingStats(),

    // Testimonials
    val testimonials: List<Testimonial> = DefaultTestimonials.testimonials,

    // Feature Comparison
    val featureComparisons: List<FeatureComparison> = DefaultFeatureComparisons.comparisons,

    // How It Works
    val howItWorksSteps: List<HowItWorksStep> = DefaultHowItWorksSteps.steps,

    // Trust Pillars
    val trustPillars: List<TrustPillar> = DefaultTrustPillars.pillars,

    // Navigation Menu (Mobile)
    val isMobileMenuOpen: Boolean = false,

    // Error State
    val errorMessage: String? = null
)

sealed class LandingEvent {
    data class UpdateSendAmount(val amount: Double) : LandingEvent()
    data class SelectCurrency(val currency: Currency) : LandingEvent()
    data object Compare : LandingEvent()
    data object ClearResults : LandingEvent()
    data object ToggleLanguage : LandingEvent()
    data object ToggleMobileMenu : LandingEvent()
    data object DismissError : LandingEvent()
    data class TrackProviderClick(
        val providerId: String,
        val providerName: String,
        val amount: Double,
        val currency: String
    ) : LandingEvent()
}

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val getComparisonUseCase: GetComparisonUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LandingUiState())
    val uiState: StateFlow<LandingUiState> = _uiState.asStateFlow()

    fun onEvent(event: LandingEvent) {
        when (event) {
            is LandingEvent.UpdateSendAmount -> updateSendAmount(event.amount)
            is LandingEvent.SelectCurrency -> selectCurrency(event.currency)
            is LandingEvent.Compare -> performComparison()
            is LandingEvent.ClearResults -> clearResults()
            is LandingEvent.ToggleLanguage -> toggleLanguage()
            is LandingEvent.ToggleMobileMenu -> toggleMobileMenu()
            is LandingEvent.DismissError -> dismissError()
            is LandingEvent.TrackProviderClick -> trackProviderClick(
                event.providerId,
                event.providerName,
                event.amount,
                event.currency
            )
        }
    }

    private fun updateSendAmount(amount: Double) {
        _uiState.update { it.copy(sendAmount = amount.coerceIn(1.0, 100000.0)) }
    }

    private fun selectCurrency(currency: Currency) {
        _uiState.update {
            it.copy(
                selectedCurrency = currency,
                showCompareResults = false,
                compareResults = emptyList()
            )
        }
    }

    private fun performComparison() {
        viewModelScope.launch {
            _uiState.update { it.copy(isComparing = true, errorMessage = null) }

            try {
                // Simulate API call with mock data for landing page
                delay(1500) // Simulated network delay

                val mockResults = generateMockCompareResults(
                    _uiState.value.sendAmount,
                    _uiState.value.selectedCurrency.code
                )

                _uiState.update {
                    it.copy(
                        isComparing = false,
                        compareResults = mockResults,
                        showCompareResults = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isComparing = false,
                        errorMessage = "Unable to fetch rates. Please try again."
                    )
                }
            }
        }
    }

    private fun generateMockCompareResults(amount: Double, currencyCode: String): List<QuickCompareResult> {
        // Base exchange rates (approximate real rates)
        val baseRates = mapOf(
            "AUD" to 36.52,
            "USD" to 55.80,
            "AED" to 15.20,
            "SGD" to 41.30,
            "GBP" to 70.45,
            "SAR" to 14.88,
            "CAD" to 41.15,
            "HKD" to 7.15,
            "EUR" to 60.25,
            "JPY" to 0.38
        )

        val baseRate = baseRates[currencyCode] ?: 50.0

        val providers = listOf(
            Triple("wise", "Wise", 0.0 to 0.005), // No fee, +0.5% rate variance
            Triple("remitly", "Remitly", 3.99 to 0.003),
            Triple("western_union", "Western Union", 5.99 to -0.002),
            Triple("worldremit", "WorldRemit", 2.99 to 0.001),
            Triple("moneygram", "MoneyGram", 4.99 to -0.005),
            Triple("xoom", "Xoom", 0.0 to -0.003)
        )

        val speeds = listOf("Instant", "Within minutes", "1-2 hours", "Same day", "1 business day")

        return providers.mapIndexed { index, (id, name, feeAndVariance) ->
            val (fee, rateVariance) = feeAndVariance
            val adjustedRate = baseRate * (1 + rateVariance)
            val recipientGets = (amount - fee) * adjustedRate

            QuickCompareResult(
                providerId = id,
                providerName = name,
                exchangeRate = adjustedRate,
                transferFee = fee,
                recipientGets = recipientGets,
                transferSpeed = speeds[index % speeds.size],
                isBestRate = false,
                affiliateUrl = "https://aipadala.com/go/$id"
            )
        }.sortedByDescending { it.recipientGets }
            .mapIndexed { index, result ->
                result.copy(isBestRate = index == 0)
            }
    }

    private fun clearResults() {
        _uiState.update {
            it.copy(
                showCompareResults = false,
                compareResults = emptyList()
            )
        }
    }

    private fun toggleLanguage() {
        _uiState.update {
            it.copy(
                currentLanguage = if (it.currentLanguage == "en") "fil" else "en"
            )
        }
    }

    private fun toggleMobileMenu() {
        _uiState.update { it.copy(isMobileMenuOpen = !it.isMobileMenuOpen) }
    }

    private fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun trackProviderClick(
        providerId: String,
        providerName: String,
        amount: Double,
        currency: String
    ) {
        // Track analytics event
        viewModelScope.launch {
            // In production, this would call analytics service
            // analyticsTracker.trackProviderClick(providerId, providerName, amount, currency)
        }
    }
}
