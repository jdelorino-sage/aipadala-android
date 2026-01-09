package com.aipadala.android.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aipadala.android.core.database.entities.CachedRateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RatesDao {

    @Query("SELECT * FROM cached_rates WHERE fromCurrency = :from AND toCurrency = :to ORDER BY rate DESC")
    fun getRates(from: String, to: String): Flow<List<CachedRateEntity>>

    @Query("SELECT * FROM cached_rates WHERE fromCurrency = :from AND toCurrency = :to ORDER BY rate DESC")
    suspend fun getRatesSync(from: String, to: String): List<CachedRateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<CachedRateEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRate(rate: CachedRateEntity)

    @Query("DELETE FROM cached_rates WHERE timestamp < :threshold")
    suspend fun deleteOldRates(threshold: Long)

    @Query("SELECT * FROM cached_rates WHERE timestamp > :threshold")
    fun getRecentRates(threshold: Long): Flow<List<CachedRateEntity>>

    @Query("SELECT * FROM cached_rates WHERE provider = :provider AND fromCurrency = :from AND toCurrency = :to LIMIT 1")
    suspend fun getRateByProvider(provider: String, from: String, to: String): CachedRateEntity?

    @Query("DELETE FROM cached_rates")
    suspend fun clearAll()
}
