package com.aipadala.android.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RateResponse(
    @Json(name = "source") val source: String,
    @Json(name = "target") val target: String,
    @Json(name = "rate") val rate: Double,
    @Json(name = "timestamp") val timestamp: String
)

@JsonClass(generateAdapter = true)
data class RateHistoryResponse(
    @Json(name = "source") val source: String,
    @Json(name = "target") val target: String,
    @Json(name = "dataPoints") val dataPoints: List<RateDataPointDto>
)

@JsonClass(generateAdapter = true)
data class RateDataPointDto(
    @Json(name = "timestamp") val timestamp: String,
    @Json(name = "rate") val rate: Double
)
