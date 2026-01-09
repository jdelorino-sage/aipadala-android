package com.aipadala.android.data.repository

import android.util.Log
import com.aipadala.android.core.database.dao.RatesDao
import com.aipadala.android.core.database.entities.CachedRateEntity
import com.aipadala.android.core.network.NetworkMonitor
import com.aipadala.android.core.util.Constants
import com.aipadala.android.core.util.Resource
import com.aipadala.android.data.model.ComparisonResult
import com.aipadala.android.data.model.ComparisonSummary
import com.aipadala.android.data.model.ExchangeRate
import com.aipadala.android.data.model.PayoutMethod
import com.aipadala.android.data.model.RateDataPoint
import com.aipadala.android.data.model.RateHistory
import com.aipadala.android.data.model.RemittanceProvider
import com.aipadala.android.data.model.SupportedCurrencies
import com.aipadala.android.data.remote.api.RatesApi
import com.aipadala.android.data.remote.dto.ComparisonResponse
import com.aipadala.android.data.remote.dto.ProviderQuoteDto
import com.aipadala.android.domain.repository.RatesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RatesRepositoryImpl @Inject constructor(
    private val ratesApi: RatesApi,
    private val ratesDao: RatesDao,
    private val networkMonitor: NetworkMonitor
) : RatesRepository {

    companion object {
        private const val TAG = "RatesRepository"
    }

    override fun getComparison(
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): Flow<Resource<ComparisonSummary>> = flow {
        emit(Resource.Loading)

        // Always emit cached data first for better UX
        val cachedRates = ratesDao.getRatesSync(fromCurrency, toCurrency)
        if (cachedRates.isNotEmpty()) {
            val cachedSummary = mapCachedToSummary(cachedRates, fromCurrency, toCurrency, amount)
            emit(Resource.Success(cachedSummary))
        }

        // Then try to fetch fresh data if online
        if (networkMonitor.isCurrentlyOnline()) {
            try {
                val response = ratesApi.getComparison(fromCurrency, toCurrency, amount)

                // Cache the response
                val entities = mapResponseToEntities(response)
                ratesDao.insertRates(entities)

                // Emit fresh data
                val summary = mapResponseToSummary(response, amount)
                emit(Resource.Success(summary))
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch comparison for $fromCurrency→$toCurrency: ${e.message}", e)
                if (cachedRates.isEmpty()) {
                    emit(Resource.Error("Unable to fetch rates. Please check your connection."))
                }
                // If we have cached data, we've already emitted it
            }
        } else if (cachedRates.isEmpty()) {
            emit(Resource.Error("You're offline and no cached rates are available."))
        }
    }

    override suspend fun getLatestRate(
        fromCurrency: String,
        toCurrency: String
    ): ExchangeRate {
        return try {
            val response = ratesApi.getExchangeRate(fromCurrency, toCurrency)
            ExchangeRate(
                fromCurrency = response.source,
                toCurrency = response.target,
                rate = response.rate,
                timestamp = System.currentTimeMillis()
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get latest rate for $fromCurrency→$toCurrency: ${e.message}", e)
            // Return cached rate if available
            val cached = ratesDao.getRatesSync(fromCurrency, toCurrency).firstOrNull()
            ExchangeRate(
                fromCurrency = fromCurrency,
                toCurrency = toCurrency,
                rate = cached?.rate ?: 0.0,
                timestamp = cached?.timestamp ?: 0L
            )
        }
    }

    override fun getRateHistory(
        fromCurrency: String,
        toCurrency: String,
        days: Int
    ): Flow<Resource<RateHistory>> = flow {
        emit(Resource.Loading)

        try {
            val response = ratesApi.getRateHistory(fromCurrency, toCurrency, days)
            val history = RateHistory(
                fromCurrency = response.source,
                toCurrency = response.target,
                dataPoints = response.dataPoints.map { dto ->
                    RateDataPoint(
                        timestamp = parseTimestamp(dto.timestamp),
                        rate = dto.rate
                    )
                }
            )
            emit(Resource.Success(history))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get rate history for $fromCurrency→$toCurrency: ${e.message}", e)
            emit(Resource.Error("Unable to fetch rate history."))
        }
    }

    override suspend fun cacheLatestRates() = coroutineScope {
        // Fetch rates in parallel for all popular currencies
        val jobs = SupportedCurrencies.POPULAR_CURRENCIES.map { currency ->
            async {
                try {
                    val response = ratesApi.getComparison(currency.code, "PHP", Constants.DEFAULT_SEND_AMOUNT)
                    val entities = mapResponseToEntities(response)
                    ratesDao.insertRates(entities)
                    Log.d(TAG, "Successfully cached rates for ${currency.code}")
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to cache rates for ${currency.code}: ${e.message}")
                }
            }
        }

        // Wait for all parallel fetches to complete
        jobs.awaitAll()

        // Clean old cache (older than 24 hours)
        val threshold = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(Constants.CACHE_DURATION_HOURS)
        ratesDao.deleteOldRates(threshold)
        Log.d(TAG, "Cleaned up old cached rates")
    }

    private fun mapResponseToEntities(response: ComparisonResponse): List<CachedRateEntity> {
        return response.providers.map { quote ->
            CachedRateEntity(
                id = "${response.sourceCurrency}_${response.targetCurrency}_${quote.provider}",
                fromCurrency = response.sourceCurrency,
                toCurrency = response.targetCurrency,
                provider = quote.provider,
                rate = quote.rate,
                fee = quote.fee,
                recipientGets = quote.recipientGets,
                deliveryTime = quote.deliveryTime,
                payoutMethods = quote.payoutMethods,
                timestamp = System.currentTimeMillis()
            )
        }
    }

    private fun mapResponseToSummary(response: ComparisonResponse, amount: Double): ComparisonSummary {
        val results = response.providers.mapNotNull { quote ->
            mapQuoteToResult(quote, response.sourceCurrency, response.targetCurrency, amount)
        }.sortedByDescending { it.recipientGets }

        val bestProvider = results.firstOrNull()
        val averageRate = if (results.isNotEmpty()) {
            results.map { it.rate }.average()
        } else 0.0

        val savingsVsBest = if (bestProvider != null && results.size > 1) {
            val worstRecipientGets = results.last().recipientGets
            bestProvider.recipientGets - worstRecipientGets
        } else 0.0

        return ComparisonSummary(
            fromCurrency = response.sourceCurrency,
            toCurrency = response.targetCurrency,
            sendAmount = amount,
            results = results,
            bestProvider = bestProvider,
            averageRate = averageRate,
            savingsVsBest = savingsVsBest,
            timestamp = System.currentTimeMillis()
        )
    }

    private fun mapCachedToSummary(
        cached: List<CachedRateEntity>,
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): ComparisonSummary {
        val results = cached.mapNotNull { entity ->
            val provider = RemittanceProvider.getByName(entity.provider) ?: return@mapNotNull null
            ComparisonResult(
                provider = provider,
                fromCurrency = entity.fromCurrency,
                toCurrency = entity.toCurrency,
                sendAmount = amount,
                rate = entity.rate,
                fee = entity.fee,
                recipientGets = (amount - entity.fee) * entity.rate,
                deliveryTime = entity.deliveryTime,
                payoutMethods = entity.payoutMethods.mapNotNull { PayoutMethod.entries.find { pm -> pm.name == it } },
                trustScore = 4, // Default
                affiliateUrl = ""
            )
        }.sortedByDescending { it.recipientGets }

        val bestProvider = results.firstOrNull()
        val averageRate = if (results.isNotEmpty()) {
            results.map { it.rate }.average()
        } else 0.0

        return ComparisonSummary(
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            sendAmount = amount,
            results = results,
            bestProvider = bestProvider,
            averageRate = averageRate,
            savingsVsBest = 0.0,
            timestamp = cached.firstOrNull()?.timestamp ?: System.currentTimeMillis()
        )
    }

    private fun mapQuoteToResult(
        quote: ProviderQuoteDto,
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): ComparisonResult? {
        val provider = RemittanceProvider.getByName(quote.provider) ?: return null
        return ComparisonResult(
            provider = provider,
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            sendAmount = amount,
            rate = quote.rate,
            fee = quote.fee,
            recipientGets = quote.recipientGets,
            deliveryTime = quote.deliveryTime,
            payoutMethods = quote.payoutMethods.mapNotNull { name ->
                PayoutMethod.entries.find { it.name.equals(name, ignoreCase = true) }
            },
            trustScore = quote.trustScore ?: 4,
            affiliateUrl = "" // Will be built by AffiliateLinkHandler
        )
    }

    private fun parseTimestamp(timestamp: String): Long {
        return try {
            java.time.Instant.parse(timestamp).toEpochMilli()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
}
