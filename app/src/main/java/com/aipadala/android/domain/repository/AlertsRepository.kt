package com.aipadala.android.domain.repository

import com.aipadala.android.data.model.AlertType
import com.aipadala.android.data.model.RateAlert
import kotlinx.coroutines.flow.Flow

interface AlertsRepository {

    fun getAllAlerts(): Flow<List<RateAlert>>

    fun getActiveAlerts(): Flow<List<RateAlert>>

    suspend fun getActiveAlertsSync(): List<RateAlert>

    suspend fun getAlertById(id: String): RateAlert?

    suspend fun createAlert(
        fromCurrency: String,
        toCurrency: String,
        threshold: Double,
        type: AlertType
    ): Result<RateAlert>

    suspend fun updateAlert(alert: RateAlert)

    suspend fun deleteAlert(id: String)

    suspend fun setAlertActive(id: String, isActive: Boolean)

    suspend fun markAlertTriggered(id: String)
}
