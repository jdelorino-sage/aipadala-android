package com.aipadala.android.presentation.screens.comparison

import androidx.lifecycle.ViewModel
import com.aipadala.android.core.util.Constants
import com.aipadala.android.data.model.Currency
import com.aipadala.android.data.model.PayoutMethod
import com.aipadala.android.data.model.SupportedCurrencies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ComparisonUiState(
    val amount: Double = Constants.DEFAULT_SEND_AMOUNT,
    val fromCurrency: Currency = SupportedCurrencies.SOURCE_CURRENCIES.first { it.code == "USD" },
    val toCurrency: Currency = SupportedCurrencies.PHP,
    val selectedPayoutMethods: Set<PayoutMethod> = emptySet(),
    val showCurrencySelector: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val isValid: Boolean
        get() = amount >= Constants.MIN_SEND_AMOUNT && amount <= Constants.MAX_SEND_AMOUNT
}

@HiltViewModel
class ComparisonViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ComparisonUiState())
    val uiState: StateFlow<ComparisonUiState> = _uiState.asStateFlow()

    fun updateAmount(amount: Double) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun updateFromCurrency(currency: Currency) {
        _uiState.update { it.copy(fromCurrency = currency) }
    }

    fun showCurrencySelector(show: Boolean) {
        _uiState.update { it.copy(showCurrencySelector = show) }
    }

    fun togglePayoutMethod(method: PayoutMethod) {
        _uiState.update { state ->
            val newMethods = if (state.selectedPayoutMethods.contains(method)) {
                state.selectedPayoutMethods - method
            } else {
                state.selectedPayoutMethods + method
            }
            state.copy(selectedPayoutMethods = newMethods)
        }
    }
}
