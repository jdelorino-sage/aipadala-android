package com.aipadala.android.presentation.screens.comparison

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aipadala.android.core.util.Resource
import com.aipadala.android.data.model.ComparisonResult
import com.aipadala.android.data.model.ComparisonSummary
import com.aipadala.android.domain.usecase.comparison.GetComparisonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ComparisonResultUiState(
    val isLoading: Boolean = false,
    val results: List<ComparisonResult> = emptyList(),
    val comparisonSummary: ComparisonSummary? = null,
    val sortBy: SortOption = SortOption.RECIPIENT_GETS,
    val error: String? = null
)

enum class SortOption {
    RECIPIENT_GETS,
    RATE,
    FEE,
    SPEED
}

@HiltViewModel
class ComparisonResultViewModel @Inject constructor(
    private val getComparisonUseCase: GetComparisonUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ComparisonResultUiState())
    val uiState: StateFlow<ComparisonResultUiState> = _uiState.asStateFlow()

    fun loadComparison(fromCurrency: String, toCurrency: String, amount: Double) {
        viewModelScope.launch {
            getComparisonUseCase(fromCurrency, toCurrency, amount).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                results = sortResults(resource.data.results, it.sortBy),
                                comparisonSummary = resource.data,
                                error = null
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateSort(sortOption: SortOption) {
        _uiState.update {
            it.copy(
                sortBy = sortOption,
                results = sortResults(it.results, sortOption)
            )
        }
    }

    private fun sortResults(
        results: List<ComparisonResult>,
        sortOption: SortOption
    ): List<ComparisonResult> {
        return when (sortOption) {
            SortOption.RECIPIENT_GETS -> results.sortedByDescending { it.recipientGets }
            SortOption.RATE -> results.sortedByDescending { it.rate }
            SortOption.FEE -> results.sortedBy { it.fee }
            SortOption.SPEED -> results.sortedBy {
                when {
                    it.deliveryTime.contains("minute", ignoreCase = true) -> 1
                    it.deliveryTime.contains("hour", ignoreCase = true) -> 2
                    it.deliveryTime.contains("day", ignoreCase = true) -> 3
                    else -> 4
                }
            }
        }
    }
}
