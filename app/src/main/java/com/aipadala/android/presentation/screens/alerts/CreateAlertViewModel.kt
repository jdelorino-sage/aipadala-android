package com.aipadala.android.presentation.screens.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aipadala.android.data.model.AlertType
import com.aipadala.android.data.model.Currency
import com.aipadala.android.data.model.SupportedCurrencies
import com.aipadala.android.domain.usecase.alerts.CreateAlertUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateAlertUiState(
    val fromCurrency: Currency = SupportedCurrencies.SOURCE_CURRENCIES.first { it.code == "USD" },
    val alertType: AlertType = AlertType.ABOVE,
    val threshold: String = "",
    val showCurrencySelector: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val isValid: Boolean
        get() = threshold.toDoubleOrNull()?.let { it > 0 } ?: false
}

@HiltViewModel
class CreateAlertViewModel @Inject constructor(
    private val createAlertUseCase: CreateAlertUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateAlertUiState())
    val uiState: StateFlow<CreateAlertUiState> = _uiState.asStateFlow()

    fun setFromCurrency(currency: Currency) {
        _uiState.update { it.copy(fromCurrency = currency) }
    }

    fun setAlertType(type: AlertType) {
        _uiState.update { it.copy(alertType = type) }
    }

    fun setThreshold(value: String) {
        _uiState.update { it.copy(threshold = value) }
    }

    fun showCurrencySelector(show: Boolean) {
        _uiState.update { it.copy(showCurrencySelector = show) }
    }

    fun createAlert() {
        val threshold = _uiState.value.threshold.toDoubleOrNull() ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            createAlertUseCase(
                fromCurrency = _uiState.value.fromCurrency.code,
                toCurrency = "PHP",
                threshold = threshold,
                type = _uiState.value.alertType
            ).onSuccess {
                _uiState.update { it.copy(isLoading = false) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }
}
