package com.aipadala.android.domain.usecase.favorites

import com.aipadala.android.data.model.Currency
import com.aipadala.android.data.model.FavoriteCorridor
import com.aipadala.android.domain.repository.FavoritesRepository
import javax.inject.Inject

class ManageFavoritesUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {
    suspend fun addFavorite(
        fromCurrency: Currency,
        toCurrency: Currency,
        defaultAmount: Double
    ): Result<FavoriteCorridor> {
        return favoritesRepository.addFavorite(fromCurrency, toCurrency, defaultAmount)
    }

    suspend fun removeFavorite(id: String) {
        favoritesRepository.removeFavorite(id)
    }

    suspend fun reorderFavorites(orderedIds: List<String>) {
        favoritesRepository.reorderFavorites(orderedIds)
    }

    suspend fun updateAmount(id: String, amount: Double) {
        if (amount > 0) {
            favoritesRepository.updateFavoriteAmount(id, amount)
        }
    }

    suspend fun isFavorite(fromCurrency: String, toCurrency: String): Boolean {
        return favoritesRepository.isFavorite(fromCurrency, toCurrency)
    }
}
