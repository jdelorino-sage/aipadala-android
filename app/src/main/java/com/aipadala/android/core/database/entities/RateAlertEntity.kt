package com.aipadala.android.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rate_alerts")
data class RateAlertEntity(
    @PrimaryKey
    val id: String,
    val fromCurrency: String,
    val toCurrency: String,
    val threshold: Double,
    val alertType: String, // "above" or "below"
    val isActive: Boolean = true,
    val lastTriggered: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
