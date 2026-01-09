package com.aipadala.android.domain.usecase.rates

import com.aipadala.android.core.util.Resource
import com.aipadala.android.data.model.RateHistory
import com.aipadala.android.domain.repository.RatesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRateHistoryUseCase @Inject constructor(
    private val ratesRepository: RatesRepository
) {
    operator fun invoke(
        fromCurrency: String,
        toCurrency: String = "PHP",
        days: Int = 30
    ): Flow<Resource<RateHistory>> {
        return ratesRepository.getRateHistory(fromCurrency, toCurrency, days)
    }
}
