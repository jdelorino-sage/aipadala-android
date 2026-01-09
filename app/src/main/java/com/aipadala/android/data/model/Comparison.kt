package com.aipadala.android.data.model

data class ComparisonResult(
    val provider: RemittanceProvider,
    val fromCurrency: String,
    val toCurrency: String,
    val sendAmount: Double,
    val rate: Double,
    val fee: Double,
    val recipientGets: Double,
    val deliveryTime: String,
    val payoutMethods: List<PayoutMethod>,
    val trustScore: Int, // 1-5
    val affiliateUrl: String
)

data class ComparisonSummary(
    val fromCurrency: String,
    val toCurrency: String,
    val sendAmount: Double,
    val results: List<ComparisonResult>,
    val bestProvider: ComparisonResult?,
    val averageRate: Double,
    val savingsVsBest: Double,
    val timestamp: Long
) {
    val worstProvider: ComparisonResult? = results.minByOrNull { it.recipientGets }

    val potentialSavings: Double
        get() = if (bestProvider != null && worstProvider != null) {
            bestProvider.recipientGets - worstProvider.recipientGets
        } else 0.0
}

data class ExchangeRate(
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double,
    val timestamp: Long
)

data class RateHistory(
    val fromCurrency: String,
    val toCurrency: String,
    val dataPoints: List<RateDataPoint>
)

data class RateDataPoint(
    val timestamp: Long,
    val rate: Double
)
