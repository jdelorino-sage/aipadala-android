package com.aipadala.android.data.remote.api

import com.aipadala.android.data.remote.dto.ComparisonResponse
import com.aipadala.android.data.remote.dto.RateHistoryResponse
import com.aipadala.android.data.remote.dto.RateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApi {

    @GET("v1/comparisons")
    suspend fun getComparison(
        @Query("sourceCurrency") sourceCurrency: String,
        @Query("targetCurrency") targetCurrency: String = "PHP",
        @Query("sendAmount") sendAmount: Double
    ): ComparisonResponse

    @GET("v1/rates")
    suspend fun getExchangeRate(
        @Query("source") source: String,
        @Query("target") target: String = "PHP"
    ): RateResponse

    @GET("v1/rates/history")
    suspend fun getRateHistory(
        @Query("source") source: String,
        @Query("target") target: String = "PHP",
        @Query("days") days: Int = 30
    ): RateHistoryResponse

    @GET("v1/rates/latest")
    suspend fun getLatestRates(
        @Query("sources") sources: String, // Comma-separated currency codes
        @Query("target") target: String = "PHP"
    ): List<RateResponse>
}
