package com.aipadala.android.data.repository

import com.aipadala.android.core.database.dao.AlertsDao
import com.aipadala.android.core.database.entities.RateAlertEntity
import com.aipadala.android.core.util.Constants
import com.aipadala.android.data.model.AlertType
import com.aipadala.android.data.model.RateAlert
import com.aipadala.android.domain.repository.AlertsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertsRepositoryImpl @Inject constructor(
    private val alertsDao: AlertsDao
) : AlertsRepository {

    override fun getAllAlerts(): Flow<List<RateAlert>> {
        return alertsDao.getAllAlerts().map { entities ->
            entities.map { mapEntityToModel(it) }
        }
    }

    override fun getActiveAlerts(): Flow<List<RateAlert>> {
        return alertsDao.getActiveAlerts().map { entities ->
            entities.map { mapEntityToModel(it) }
        }
    }

    override suspend fun getActiveAlertsSync(): List<RateAlert> {
        return alertsDao.getActiveAlertsSync().map { mapEntityToModel(it) }
    }

    override suspend fun getAlertById(id: String): RateAlert? {
        return alertsDao.getAlertById(id)?.let { mapEntityToModel(it) }
    }

    override suspend fun createAlert(
        fromCurrency: String,
        toCurrency: String,
        threshold: Double,
        type: AlertType
    ): Result<RateAlert> {
        val activeCount = alertsDao.getActiveAlertsCount()
        if (activeCount >= Constants.MAX_ACTIVE_ALERTS) {
            return Result.failure(Exception("Maximum of ${Constants.MAX_ACTIVE_ALERTS} active alerts allowed"))
        }

        val entity = RateAlertEntity(
            id = UUID.randomUUID().toString(),
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            threshold = threshold,
            alertType = type.name.lowercase(),
            isActive = true
        )

        alertsDao.insertAlert(entity)
        return Result.success(mapEntityToModel(entity))
    }

    override suspend fun updateAlert(alert: RateAlert) {
        val entity = mapModelToEntity(alert)
        alertsDao.updateAlert(entity)
    }

    override suspend fun deleteAlert(id: String) {
        alertsDao.deleteAlertById(id)
    }

    override suspend fun setAlertActive(id: String, isActive: Boolean) {
        alertsDao.setAlertActive(id, isActive)
    }

    override suspend fun markAlertTriggered(id: String) {
        alertsDao.markAlertTriggered(id, System.currentTimeMillis())
    }

    private fun mapEntityToModel(entity: RateAlertEntity): RateAlert {
        return RateAlert(
            id = entity.id,
            fromCurrency = entity.fromCurrency,
            toCurrency = entity.toCurrency,
            threshold = entity.threshold,
            type = if (entity.alertType == "above") AlertType.ABOVE else AlertType.BELOW,
            isActive = entity.isActive,
            lastTriggered = entity.lastTriggered,
            createdAt = entity.createdAt
        )
    }

    private fun mapModelToEntity(alert: RateAlert): RateAlertEntity {
        return RateAlertEntity(
            id = alert.id,
            fromCurrency = alert.fromCurrency,
            toCurrency = alert.toCurrency,
            threshold = alert.threshold,
            alertType = alert.type.name.lowercase(),
            isActive = alert.isActive,
            lastTriggered = alert.lastTriggered,
            createdAt = alert.createdAt
        )
    }
}
