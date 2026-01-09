package com.aipadala.android.presentation.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aipadala.android.data.model.FavoriteCorridor
import com.aipadala.android.domain.usecase.favorites.GetFavoritesUseCase
import com.aipadala.android.domain.usecase.favorites.ManageFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val isLoading: Boolean = false,
    val favorites: List<FavoriteCorridor> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val manageFavoritesUseCase: ManageFavoritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            getFavoritesUseCase().collect { favorites ->
                _uiState.update { it.copy(favorites = favorites, isLoading = false) }
            }
        }
    }

    fun removeFavorite(id: String) {
        viewModelScope.launch {
            manageFavoritesUseCase.removeFavorite(id)
        }
    }

    fun reorderFavorites(orderedIds: List<String>) {
        viewModelScope.launch {
            manageFavoritesUseCase.reorderFavorites(orderedIds)
        }
    }
}
