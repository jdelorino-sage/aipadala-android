package com.aipadala.android.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aipadala.android.data.model.FavoriteCorridor
import com.aipadala.android.domain.usecase.favorites.GetFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val favoriteCorridors: List<FavoriteCorridor> = emptyList(),
    val dailyTip: String? = null,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val tips = listOf(
        "Compare rates from multiple providers before sending. You could save up to ₱500 per transaction!",
        "Set up rate alerts to get notified when rates reach your target. Timing can save you money!",
        "Consider using bank deposit instead of cash pickup for better rates.",
        "Send larger amounts less frequently to minimize total fees.",
        "Check delivery times - express transfers often cost more."
    )

    init {
        loadFavorites()
        loadDailyTip()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            getFavoritesUseCase().collect { favorites ->
                _uiState.update { it.copy(favoriteCorridors = favorites) }
            }
        }
    }

    private fun loadDailyTip() {
        // Rotate tips based on day of week
        val dayOfYear = java.time.LocalDate.now().dayOfYear
        val tipIndex = dayOfYear % tips.size
        _uiState.update { it.copy(dailyTip = tips[tipIndex]) }
    }
}
