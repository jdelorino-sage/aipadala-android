package com.aipadala.android.domain.repository

import com.aipadala.android.data.model.Currency
import com.aipadala.android.data.model.FavoriteCorridor
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun getAllFavorites(): Flow<List<FavoriteCorridor>>

    suspend fun getFavoriteById(id: String): FavoriteCorridor?

    suspend fun addFavorite(
        fromCurrency: Currency,
        toCurrency: Currency,
        defaultAmount: Double
    ): Result<FavoriteCorridor>

    suspend fun removeFavorite(id: String)

    suspend fun updateFavoritePosition(id: String, newPosition: Int)

    suspend fun updateFavoriteAmount(id: String, amount: Double)

    suspend fun isFavorite(fromCurrency: String, toCurrency: String): Boolean

    suspend fun reorderFavorites(orderedIds: List<String>)
}
