package com.aipadala.android.data.model

data class FavoriteCorridor(
    val id: String,
    val fromCurrency: Currency,
    val toCurrency: Currency,
    val displayName: String,
    val defaultAmount: Double,
    val position: Int,
    val currentRate: Double? = null,
    val rateChange: Double? = null, // Percentage change
    val sparklineData: List<Double>? = null
)
