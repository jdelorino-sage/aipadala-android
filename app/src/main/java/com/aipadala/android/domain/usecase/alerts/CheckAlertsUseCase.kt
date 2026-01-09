package com.aipadala.android.domain.usecase.alerts

import com.aipadala.android.data.model.AlertType
import com.aipadala.android.data.model.RateAlert
import com.aipadala.android.domain.repository.AlertsRepository
import com.aipadala.android.domain.repository.RatesRepository
import javax.inject.Inject

data class TriggeredAlert(
    val alert: RateAlert,
    val currentRate: Double
)

class CheckAlertsUseCase @Inject constructor(
    private val alertsRepository: AlertsRepository,
    private val ratesRepository: RatesRepository
) {
    suspend operator fun invoke(): List<TriggeredAlert> {
        val activeAlerts = alertsRepository.getActiveAlertsSync()
        val triggeredAlerts = mutableListOf<TriggeredAlert>()

        activeAlerts.forEach { alert ->
            val currentRate = ratesRepository.getLatestRate(
                alert.fromCurrency,
                alert.toCurrency
            )

            val shouldTrigger = when (alert.type) {
                AlertType.ABOVE -> currentRate.rate >= alert.threshold
                AlertType.BELOW -> currentRate.rate <= alert.threshold
            }

            if (shouldTrigger) {
                triggeredAlerts.add(
                    TriggeredAlert(
                        alert = alert,
                        currentRate = currentRate.rate
                    )
                )
                // Mark as triggered
                alertsRepository.markAlertTriggered(alert.id)
            }
        }

        return triggeredAlerts
    }
}
