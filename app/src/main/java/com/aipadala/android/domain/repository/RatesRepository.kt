package com.aipadala.android.domain.repository

import com.aipadala.android.core.util.Resource
import com.aipadala.android.data.model.ComparisonSummary
import com.aipadala.android.data.model.ExchangeRate
import com.aipadala.android.data.model.RateHistory
import kotlinx.coroutines.flow.Flow

interface RatesRepository {

    fun getComparison(
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): Flow<Resource<ComparisonSummary>>

    suspend fun getLatestRate(
        fromCurrency: String,
        toCurrency: String
    ): ExchangeRate

    fun getRateHistory(
        fromCurrency: String,
        toCurrency: String,
        days: Int = 30
    ): Flow<Resource<RateHistory>>

    suspend fun cacheLatestRates()
}
