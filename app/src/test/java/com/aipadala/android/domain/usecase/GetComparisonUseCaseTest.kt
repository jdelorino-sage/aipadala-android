package com.aipadala.android.domain.usecase

import com.aipadala.android.core.util.AffiliateLinkHandler
import com.aipadala.android.core.util.Resource
import com.aipadala.android.data.model.ComparisonResult
import com.aipadala.android.data.model.ComparisonSummary
import com.aipadala.android.data.model.PayoutMethod
import com.aipadala.android.data.model.RemittanceProvider
import com.aipadala.android.domain.repository.RatesRepository
import com.aipadala.android.domain.usecase.comparison.GetComparisonUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetComparisonUseCaseTest {

    private lateinit var ratesRepository: RatesRepository
    private lateinit var affiliateLinkHandler: AffiliateLinkHandler
    private lateinit var getComparisonUseCase: GetComparisonUseCase

    @Before
    fun setup() {
        ratesRepository = mockk()
        affiliateLinkHandler = mockk()
        getComparisonUseCase = GetComparisonUseCase(ratesRepository, affiliateLinkHandler)
    }

    @Test
    fun `invoke returns Success with affiliate URLs added`() = runTest {
        // Given
        val mockResult = ComparisonResult(
            provider = RemittanceProvider.WISE,
            fromCurrency = "USD",
            toCurrency = "PHP",
            sendAmount = 500.0,
            rate = 56.5,
            fee = 5.0,
            recipientGets = 27975.0,
            deliveryTime = "1-2 hours",
            payoutMethods = listOf(PayoutMethod.BANK, PayoutMethod.GCASH),
            trustScore = 5,
            affiliateUrl = ""
        )
        val mockSummary = ComparisonSummary(
            fromCurrency = "USD",
            toCurrency = "PHP",
            sendAmount = 500.0,
            results = listOf(mockResult),
            bestProvider = mockResult,
            averageRate = 56.5,
            savingsVsBest = 100.0,
            timestamp = System.currentTimeMillis()
        )

        every {
            ratesRepository.getComparison("USD", "PHP", 500.0)
        } returns flowOf(Resource.Success(mockSummary))

        every {
            affiliateLinkHandler.buildAffiliateUrl(
                provider = RemittanceProvider.WISE,
                campaign = "comparison",
                sourceCurrency = "USD",
                amount = 500.0
            )
        } returns "https://wise.com/invite/aipadala?utm_source=aipadala"

        // When
        val results = getComparisonUseCase("USD", "PHP", 500.0).toList()

        // Then
        assertEquals(1, results.size)
        assertTrue(results[0] is Resource.Success)

        val successResult = results[0] as Resource.Success
        assertTrue(successResult.data.results[0].affiliateUrl.isNotEmpty())
        assertTrue(successResult.data.results[0].affiliateUrl.contains("wise.com"))
    }

    @Test
    fun `invoke passes through Error from repository`() = runTest {
        // Given
        every {
            ratesRepository.getComparison("USD", "PHP", 500.0)
        } returns flowOf(Resource.Error("Network error"))

        // When
        val results = getComparisonUseCase("USD", "PHP", 500.0).toList()

        // Then
        assertEquals(1, results.size)
        assertTrue(results[0] is Resource.Error)
        assertEquals("Network error", (results[0] as Resource.Error).message)
    }

    @Test
    fun `invoke passes through Loading from repository`() = runTest {
        // Given
        every {
            ratesRepository.getComparison("USD", "PHP", 500.0)
        } returns flowOf(Resource.Loading)

        // When
        val results = getComparisonUseCase("USD", "PHP", 500.0).toList()

        // Then
        assertEquals(1, results.size)
        assertTrue(results[0] is Resource.Loading)
    }

    @Test
    fun `invoke uses default PHP currency when not specified`() = runTest {
        // Given
        val mockSummary = ComparisonSummary(
            fromCurrency = "EUR",
            toCurrency = "PHP",
            sendAmount = 100.0,
            results = emptyList(),
            bestProvider = null,
            averageRate = 0.0,
            savingsVsBest = 0.0,
            timestamp = System.currentTimeMillis()
        )

        every {
            ratesRepository.getComparison("EUR", "PHP", 100.0)
        } returns flowOf(Resource.Success(mockSummary))

        // When
        val results = getComparisonUseCase("EUR", amount = 100.0).toList()

        // Then
        assertTrue(results[0] is Resource.Success)
        assertEquals("PHP", (results[0] as Resource.Success).data.toCurrency)
    }
}
