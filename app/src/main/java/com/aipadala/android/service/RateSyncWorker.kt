package com.aipadala.android.service

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.aipadala.android.R
import com.aipadala.android.core.util.Constants
import com.aipadala.android.domain.repository.RatesRepository
import com.aipadala.android.domain.usecase.alerts.CheckAlertsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class RateSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val ratesRepository: RatesRepository,
    private val checkAlertsUseCase: CheckAlertsUseCase,
    private val notificationManager: AIPadalaNotificationManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Check alerts
            val triggeredAlerts = checkAlertsUseCase()

            // Notify for triggered alerts
            triggeredAlerts.forEach { triggered ->
                notificationManager.showRateAlertNotification(
                    title = applicationContext.getString(R.string.rate_alert_triggered),
                    message = applicationContext.getString(
                        R.string.rate_alert_body,
                        triggered.alert.fromCurrency,
                        triggered.currentRate
                    ),
                    corridor = "${triggered.alert.fromCurrency}→${triggered.alert.toCurrency}",
                    rate = triggered.currentRate
                )
            }

            // Cache latest rates for offline use
            ratesRepository.cacheLatestRates()

            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    companion object {
        private const val WORK_NAME = "rate_sync"

        fun schedulePeriodicSync(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<RateSyncWorker>(
                repeatInterval = Constants.RATE_SYNC_INTERVAL_HOURS,
                repeatIntervalTimeUnit = TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    30,
                    TimeUnit.SECONDS
                )
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    request
                )
        }

        fun cancelSync(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
