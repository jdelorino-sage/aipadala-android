package com.aipadala.android.domain.usecase.rates

import com.aipadala.android.data.model.ExchangeRate
import com.aipadala.android.domain.repository.RatesRepository
import javax.inject.Inject

class GetLiveRatesUseCase @Inject constructor(
    private val ratesRepository: RatesRepository
) {
    suspend operator fun invoke(
        fromCurrency: String,
        toCurrency: String = "PHP"
    ): ExchangeRate {
        return ratesRepository.getLatestRate(fromCurrency, toCurrency)
    }
}
