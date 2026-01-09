package com.aipadala.android

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.aipadala.android.service.RateSyncWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AIPadalaApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        scheduleBackgroundWork()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)

            // Rate Alerts Channel
            val rateAlertsChannel = NotificationChannel(
                CHANNEL_RATE_ALERTS,
                getString(R.string.channel_rate_alerts),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.channel_rate_alerts_description)
                enableVibration(true)
            }

            // Promotions Channel
            val promotionsChannel = NotificationChannel(
                CHANNEL_PROMOTIONS,
                getString(R.string.channel_promotions),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(R.string.channel_promotions_description)
            }

            // Tips Channel
            val tipsChannel = NotificationChannel(
                CHANNEL_TIPS,
                getString(R.string.channel_tips),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.channel_tips_description)
            }

            notificationManager.createNotificationChannels(
                listOf(rateAlertsChannel, promotionsChannel, tipsChannel)
            )
        }
    }

    private fun scheduleBackgroundWork() {
        RateSyncWorker.schedulePeriodicSync(this)
    }

    companion object {
        const val CHANNEL_RATE_ALERTS = "rate_alerts"
        const val CHANNEL_PROMOTIONS = "promotions"
        const val CHANNEL_TIPS = "tips"
    }
}
