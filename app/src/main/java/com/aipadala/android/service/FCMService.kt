package com.aipadala.android.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationManager: AIPadalaNotificationManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Update token on server if needed
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.data.let { data ->
            when (data["type"]) {
                "rate_alert" -> handleRateAlert(data)
                "promotion" -> handlePromotion(data)
                "tip" -> handleDailyTip(data)
                else -> handleGenericNotification(message.notification)
            }
        }
    }

    private fun handleRateAlert(data: Map<String, String>) {
        val corridor = data["corridor"] ?: return
        val currentRate = data["rate"]?.toDoubleOrNull() ?: return
        val provider = data["provider"] ?: ""

        notificationManager.showRateAlertNotification(
            title = getString(com.aipadala.android.R.string.rate_alert_title),
            message = getString(
                com.aipadala.android.R.string.rate_alert_message,
                corridor,
                currentRate,
                provider
            ),
            corridor = corridor,
            rate = currentRate
        )
    }

    private fun handlePromotion(data: Map<String, String>) {
        val title = data["title"] ?: return
        val message = data["message"] ?: return
        val provider = data["provider"]

        notificationManager.showPromotionNotification(
            title = title,
            message = message,
            provider = provider
        )
    }

    private fun handleDailyTip(data: Map<String, String>) {
        val tip = data["tip"] ?: return

        notificationManager.showTipNotification(
            title = getString(com.aipadala.android.R.string.tip_of_the_day),
            message = tip
        )
    }

    private fun handleGenericNotification(notification: RemoteMessage.Notification?) {
        notification?.let {
            notificationManager.showGenericNotification(
                title = it.title ?: "",
                message = it.body ?: ""
            )
        }
    }
}
