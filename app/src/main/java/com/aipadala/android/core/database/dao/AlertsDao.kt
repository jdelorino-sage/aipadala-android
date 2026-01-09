package com.aipadala.android.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aipadala.android.core.database.entities.RateAlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertsDao {

    @Query("SELECT * FROM rate_alerts ORDER BY createdAt DESC")
    fun getAllAlerts(): Flow<List<RateAlertEntity>>

    @Query("SELECT * FROM rate_alerts WHERE isActive = 1")
    fun getActiveAlerts(): Flow<List<RateAlertEntity>>

    @Query("SELECT * FROM rate_alerts WHERE isActive = 1")
    suspend fun getActiveAlertsSync(): List<RateAlertEntity>

    @Query("SELECT * FROM rate_alerts WHERE id = :id")
    suspend fun getAlertById(id: String): RateAlertEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: RateAlertEntity)

    @Update
    suspend fun updateAlert(alert: RateAlertEntity)

    @Delete
    suspend fun deleteAlert(alert: RateAlertEntity)

    @Query("DELETE FROM rate_alerts WHERE id = :id")
    suspend fun deleteAlertById(id: String)

    @Query("UPDATE rate_alerts SET isActive = :isActive WHERE id = :id")
    suspend fun setAlertActive(id: String, isActive: Boolean)

    @Query("UPDATE rate_alerts SET lastTriggered = :timestamp WHERE id = :id")
    suspend fun markAlertTriggered(id: String, timestamp: Long)

    @Query("SELECT COUNT(*) FROM rate_alerts WHERE isActive = 1")
    suspend fun getActiveAlertsCount(): Int
}
