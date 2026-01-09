package com.aipadala.android.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ComparisonResponse(
    @Json(name = "sourceCurrency") val sourceCurrency: String,
    @Json(name = "targetCurrency") val targetCurrency: String,
    @Json(name = "sourceAmount") val sourceAmount: Double,
    @Json(name = "timestamp") val timestamp: String,
    @Json(name = "providers") val providers: List<ProviderQuoteDto>
)

@JsonClass(generateAdapter = true)
data class ProviderQuoteDto(
    @Json(name = "provider") val provider: String,
    @Json(name = "rate") val rate: Double,
    @Json(name = "fee") val fee: Double,
    @Json(name = "recipientGets") val recipientGets: Double,
    @Json(name = "deliveryTime") val deliveryTime: String,
    @Json(name = "payoutMethods") val payoutMethods: List<String>,
    @Json(name = "trustScore") val trustScore: Int? = null
)
