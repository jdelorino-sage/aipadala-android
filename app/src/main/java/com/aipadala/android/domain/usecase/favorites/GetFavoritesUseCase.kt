package com.aipadala.android.domain.usecase.favorites

import com.aipadala.android.data.model.FavoriteCorridor
import com.aipadala.android.domain.repository.FavoritesRepository
import com.aipadala.android.domain.repository.RatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val ratesRepository: RatesRepository
) {
    operator fun invoke(): Flow<List<FavoriteCorridor>> {
        return favoritesRepository.getAllFavorites()
    }

    suspend fun getWithRates(): Flow<List<FavoriteCorridor>> {
        return favoritesRepository.getAllFavorites().map { favorites ->
            favorites.map { corridor ->
                val rate = try {
                    ratesRepository.getLatestRate(
                        corridor.fromCurrency.code,
                        corridor.toCurrency.code
                    )
                } catch (e: Exception) {
                    null
                }

                corridor.copy(
                    currentRate = rate?.rate,
                    rateChange = null // Would need historical data to calculate
                )
            }
        }
    }
}
