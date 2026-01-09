package com.aipadala.android.presentation

import com.aipadala.android.core.util.Resource
import com.aipadala.android.data.model.ComparisonResult
import com.aipadala.android.data.model.ComparisonSummary
import com.aipadala.android.data.model.PayoutMethod
import com.aipadala.android.data.model.RemittanceProvider
import com.aipadala.android.domain.usecase.comparison.GetComparisonUseCase
import com.aipadala.android.presentation.screens.comparison.ComparisonResultViewModel
import com.aipadala.android.presentation.screens.comparison.SortOption
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ComparisonResultViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getComparisonUseCase: GetComparisonUseCase
    private lateinit var viewModel: ComparisonResultViewModel

    private val mockWiseResult = ComparisonResult(
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
        affiliateUrl = "https://wise.com"
    )

    private val mockRemitlyResult = ComparisonResult(
        provider = RemittanceProvider.REMITLY,
        fromCurrency = "USD",
        toCurrency = "PHP",
        sendAmount = 500.0,
        rate = 56.0,
        fee = 3.99,
        recipientGets = 27800.0,
        deliveryTime = "1 day",
        payoutMethods = listOf(PayoutMethod.BANK, PayoutMethod.GCASH, PayoutMethod.MAYA),
        trustScore = 4,
        affiliateUrl = "https://remitly.com"
    )

    private val mockWUResult = ComparisonResult(
        provider = RemittanceProvider.WESTERN_UNION,
        fromCurrency = "USD",
        toCurrency = "PHP",
        sendAmount = 500.0,
        rate = 55.5,
        fee = 10.0,
        recipientGets = 27195.0,
        deliveryTime = "Minutes",
        payoutMethods = listOf(PayoutMethod.BANK, PayoutMethod.CASH_PICKUP),
        trustScore = 4,
        affiliateUrl = "https://westernunion.com"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getComparisonUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has empty results and no loading`() = runTest {
        viewModel = ComparisonResultViewModel(getComparisonUseCase)

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertTrue(state.results.isEmpty())
        assertNull(state.comparisonSummary)
        assertNull(state.error)
        assertEquals(SortOption.RECIPIENT_GETS, state.sortBy)
    }

    @Test
    fun `loadComparison sets loading state`() = runTest {
        every {
            getComparisonUseCase("USD", "PHP", 500.0)
        } returns flowOf(Resource.Loading)

        viewModel = ComparisonResultViewModel(getComparisonUseCase)
        viewModel.loadComparison("USD", "PHP", 500.0)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `loadComparison updates results on success`() = runTest {
        val mockSummary = ComparisonSummary(
            fromCurrency = "USD",
            toCurrency = "PHP",
            sendAmount = 500.0,
            results = listOf(mockWiseResult, mockRemitlyResult),
            bestProvider = mockWiseResult,
            averageRate = 56.25,
            savingsVsBest = 175.0,
            timestamp = System.currentTimeMillis()
        )

        every {
            getComparisonUseCase("USD", "PHP", 500.0)
        } returns flowOf(Resource.Success(mockSummary))

        viewModel = ComparisonResultViewModel(getComparisonUseCase)
        viewModel.loadComparison("USD", "PHP", 500.0)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.results.size)
        assertEquals(mockWiseResult.provider, state.results[0].provider)
        assertNull(state.error)
    }

    @Test
    fun `loadComparison sets error on failure`() = runTest {
        every {
            getComparisonUseCase("USD", "PHP", 500.0)
        } returns flowOf(Resource.Error("Network error"))

        viewModel = ComparisonResultViewModel(getComparisonUseCase)
        viewModel.loadComparison("USD", "PHP", 500.0)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)
    }

    @Test
    fun `updateSort sorts by recipient gets descending`() = runTest {
        val mockSummary = ComparisonSummary(
            fromCurrency = "USD",
            toCurrency = "PHP",
            sendAmount = 500.0,
            results = listOf(mockRemitlyResult, mockWiseResult), // Out of order
            bestProvider = mockWiseResult,
            averageRate = 56.25,
            savingsVsBest = 175.0,
            timestamp = System.currentTimeMillis()
        )

        every {
            getComparisonUseCase("USD", "PHP", 500.0)
        } returns flowOf(Resource.Success(mockSummary))

        viewModel = ComparisonResultViewModel(getComparisonUseCase)
        viewModel.loadComparison("USD", "PHP", 500.0)
        advanceUntilIdle()

        viewModel.updateSort(SortOption.RECIPIENT_GETS)

        val state = viewModel.uiState.value
        assertEquals(SortOption.RECIPIENT_GETS, state.sortBy)
        // Wise has higher recipientGets, should be first
        assertEquals(RemittanceProvider.WISE, state.results[0].provider)
    }

    @Test
    fun `updateSort sorts by rate descending`() = runTest {
        val mockSummary = ComparisonSummary(
            fromCurrency = "USD",
            toCurrency = "PHP",
            sendAmount = 500.0,
            results = listOf(mockRemitlyResult, mockWiseResult, mockWUResult),
            bestProvider = mockWiseResult,
            averageRate = 56.0,
            savingsVsBest = 175.0,
            timestamp = System.currentTimeMillis()
        )

        every {
            getComparisonUseCase("USD", "PHP", 500.0)
        } returns flowOf(Resource.Success(mockSummary))

        viewModel = ComparisonResultViewModel(getComparisonUseCase)
        viewModel.loadComparison("USD", "PHP", 500.0)
        advanceUntilIdle()

        viewModel.updateSort(SortOption.RATE)

        val state = viewModel.uiState.value
        assertEquals(SortOption.RATE, state.sortBy)
        // Wise has highest rate (56.5)
        assertEquals(RemittanceProvider.WISE, state.results[0].provider)
    }

    @Test
    fun `updateSort sorts by fee ascending`() = runTest {
        val mockSummary = ComparisonSummary(
            fromCurrency = "USD",
            toCurrency = "PHP",
            sendAmount = 500.0,
            results = listOf(mockWUResult, mockWiseResult, mockRemitlyResult),
            bestProvider = mockWiseResult,
            averageRate = 56.0,
            savingsVsBest = 175.0,
            timestamp = System.currentTimeMillis()
        )

        every {
            getComparisonUseCase("USD", "PHP", 500.0)
        } returns flowOf(Resource.Success(mockSummary))

        viewModel = ComparisonResultViewModel(getComparisonUseCase)
        viewModel.loadComparison("USD", "PHP", 500.0)
        advanceUntilIdle()

        viewModel.updateSort(SortOption.FEE)

        val state = viewModel.uiState.value
        assertEquals(SortOption.FEE, state.sortBy)
        // Remitly has lowest fee (3.99)
        assertEquals(RemittanceProvider.REMITLY, state.results[0].provider)
    }

    @Test
    fun `updateSort sorts by speed`() = runTest {
        val mockSummary = ComparisonSummary(
            fromCurrency = "USD",
            toCurrency = "PHP",
            sendAmount = 500.0,
            results = listOf(mockRemitlyResult, mockWiseResult, mockWUResult),
            bestProvider = mockWiseResult,
            averageRate = 56.0,
            savingsVsBest = 175.0,
            timestamp = System.currentTimeMillis()
        )

        every {
            getComparisonUseCase("USD", "PHP", 500.0)
        } returns flowOf(Resource.Success(mockSummary))

        viewModel = ComparisonResultViewModel(getComparisonUseCase)
        viewModel.loadComparison("USD", "PHP", 500.0)
        advanceUntilIdle()

        viewModel.updateSort(SortOption.SPEED)

        val state = viewModel.uiState.value
        assertEquals(SortOption.SPEED, state.sortBy)
        // WU has "Minutes" which should be fastest
        assertEquals(RemittanceProvider.WESTERN_UNION, state.results[0].provider)
    }
}
