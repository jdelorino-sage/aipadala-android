package com.aipadala.android.domain.usecase.alerts

import com.aipadala.android.data.model.AlertType
import com.aipadala.android.data.model.RateAlert
import com.aipadala.android.domain.repository.AlertsRepository
import javax.inject.Inject

class CreateAlertUseCase @Inject constructor(
    private val alertsRepository: AlertsRepository
) {
    suspend operator fun invoke(
        fromCurrency: String,
        toCurrency: String = "PHP",
        threshold: Double,
        type: AlertType
    ): Result<RateAlert> {
        // Validate threshold
        if (threshold <= 0) {
            return Result.failure(IllegalArgumentException("Threshold must be greater than 0"))
        }

        return alertsRepository.createAlert(
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            threshold = threshold,
            type = type
        )
    }
}
