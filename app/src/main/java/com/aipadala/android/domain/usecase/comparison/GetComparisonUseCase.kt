package com.aipadala.android.domain.usecase.comparison

import com.aipadala.android.core.util.AffiliateLinkHandler
import com.aipadala.android.core.util.Resource
import com.aipadala.android.data.model.ComparisonResult
import com.aipadala.android.data.model.ComparisonSummary
import com.aipadala.android.domain.repository.RatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetComparisonUseCase @Inject constructor(
    private val ratesRepository: RatesRepository,
    private val affiliateLinkHandler: AffiliateLinkHandler
) {
    operator fun invoke(
        fromCurrency: String,
        toCurrency: String = "PHP",
        amount: Double
    ): Flow<Resource<ComparisonSummary>> {
        return ratesRepository.getComparison(fromCurrency, toCurrency, amount)
            .map { resource ->
                when (resource) {
                    is Resource.Success -> {
                        // Add affiliate URLs to results
                        val resultsWithAffiliates = resource.data.results.map { result ->
                            result.copy(
                                affiliateUrl = affiliateLinkHandler.buildAffiliateUrl(
                                    provider = result.provider,
                                    campaign = "comparison",
                                    sourceCurrency = fromCurrency,
                                    amount = amount
                                )
                            )
                        }
                        Resource.Success(
                            resource.data.copy(
                                results = resultsWithAffiliates,
                                bestProvider = resultsWithAffiliates.firstOrNull()
                            )
                        )
                    }
                    is Resource.Error -> resource
                    is Resource.Loading -> resource
                }
            }
    }
}
