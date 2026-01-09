package com.aipadala.android.data.model

data class RateAlert(
    val id: String,
    val fromCurrency: String,
    val toCurrency: String,
    val threshold: Double,
    val type: AlertType,
    val isActive: Boolean = true,
    val lastTriggered: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class AlertType {
    ABOVE,
    BELOW
}
