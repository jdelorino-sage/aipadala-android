package com.aipadala.android.data.repository

import com.aipadala.android.core.database.dao.FavoritesDao
import com.aipadala.android.core.database.entities.FavoriteCorridorEntity
import com.aipadala.android.core.util.Constants
import com.aipadala.android.data.model.Currency
import com.aipadala.android.data.model.FavoriteCorridor
import com.aipadala.android.data.model.SupportedCurrencies
import com.aipadala.android.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val favoritesDao: FavoritesDao
) : FavoritesRepository {

    override fun getAllFavorites(): Flow<List<FavoriteCorridor>> {
        return favoritesDao.getAllFavorites().map { entities ->
            entities.mapNotNull { entity ->
                mapEntityToModel(entity)
            }
        }
    }

    override suspend fun getFavoriteById(id: String): FavoriteCorridor? {
        return favoritesDao.getFavoriteById(id)?.let { mapEntityToModel(it) }
    }

    override suspend fun addFavorite(
        fromCurrency: Currency,
        toCurrency: Currency,
        defaultAmount: Double
    ): Result<FavoriteCorridor> {
        val currentCount = favoritesDao.getFavoritesCount()
        if (currentCount >= Constants.MAX_FAVORITE_CORRIDORS) {
            return Result.failure(Exception("Maximum of ${Constants.MAX_FAVORITE_CORRIDORS} corridors allowed"))
        }

        if (favoritesDao.isFavorite(fromCurrency.code, toCurrency.code)) {
            return Result.failure(Exception("This corridor is already a favorite"))
        }

        val entity = FavoriteCorridorEntity(
            id = UUID.randomUUID().toString(),
            fromCurrency = fromCurrency.code,
            toCurrency = toCurrency.code,
            displayName = "${fromCurrency.flagEmoji} ${fromCurrency.code} → ${toCurrency.flagEmoji} ${toCurrency.code}",
            defaultAmount = defaultAmount,
            position = currentCount
        )

        favoritesDao.insertFavorite(entity)
        return Result.success(mapEntityToModel(entity)!!)
    }

    override suspend fun removeFavorite(id: String) {
        favoritesDao.deleteFavoriteById(id)
    }

    override suspend fun updateFavoritePosition(id: String, newPosition: Int) {
        favoritesDao.updatePosition(id, newPosition)
    }

    override suspend fun updateFavoriteAmount(id: String, amount: Double) {
        favoritesDao.getFavoriteById(id)?.let { entity ->
            favoritesDao.updateFavorite(entity.copy(defaultAmount = amount))
        }
    }

    override suspend fun isFavorite(fromCurrency: String, toCurrency: String): Boolean {
        return favoritesDao.isFavorite(fromCurrency, toCurrency)
    }

    override suspend fun reorderFavorites(orderedIds: List<String>) {
        orderedIds.forEachIndexed { index, id ->
            favoritesDao.updatePosition(id, index)
        }
    }

    private fun mapEntityToModel(entity: FavoriteCorridorEntity): FavoriteCorridor? {
        val fromCurrency = SupportedCurrencies.getByCode(entity.fromCurrency) ?: return null
        val toCurrency = if (entity.toCurrency == "PHP") {
            SupportedCurrencies.PHP
        } else {
            SupportedCurrencies.getByCode(entity.toCurrency) ?: return null
        }

        return FavoriteCorridor(
            id = entity.id,
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            displayName = entity.displayName,
            defaultAmount = entity.defaultAmount,
            position = entity.position
        )
    }
}
