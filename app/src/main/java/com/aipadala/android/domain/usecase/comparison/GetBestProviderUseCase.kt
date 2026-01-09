package com.aipadala.android.domain.usecase.comparison

import com.aipadala.android.core.util.Resource
import com.aipadala.android.data.model.ComparisonResult
import com.aipadala.android.domain.repository.RatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBestProviderUseCase @Inject constructor(
    private val ratesRepository: RatesRepository
) {
    operator fun invoke(
        fromCurrency: String,
        toCurrency: String = "PHP",
        amount: Double
    ): Flow<Resource<ComparisonResult?>> {
        return ratesRepository.getComparison(fromCurrency, toCurrency, amount)
            .map { resource ->
                when (resource) {
                    is Resource.Success -> Resource.Success(resource.data.bestProvider)
                    is Resource.Error -> Resource.Error(resource.message)
                    is Resource.Loading -> Resource.Loading
                }
            }
    }
}
