package com.aipadala.android.core.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cached_rates",
    indices = [
        Index(value = ["fromCurrency", "toCurrency"]),
        Index(value = ["timestamp"])
    ]
)
data class CachedRateEntity(
    @PrimaryKey
    val id: String,
    val fromCurrency: String,
    val toCurrency: String,
    val provider: String,
    val rate: Double,
    val fee: Double,
    val recipientGets: Double,
    val deliveryTime: String,
    val payoutMethods: List<String>,
    val timestamp: Long = System.currentTimeMillis()
)
