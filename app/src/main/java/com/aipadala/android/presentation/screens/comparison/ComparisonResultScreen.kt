package com.aipadala.android.presentation.screens.comparison

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aipadala.android.R
import com.aipadala.android.core.util.formatCurrency
import com.aipadala.android.core.util.formatWithCommas
import com.aipadala.android.presentation.components.common.ErrorState
import com.aipadala.android.presentation.components.common.LoadingState
import com.aipadala.android.presentation.components.comparison.ProviderResultCard
import com.aipadala.android.presentation.theme.AIPadalaColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparisonResultScreen(
    fromCurrency: String,
    toCurrency: String,
    amount: Double,
    viewModel: ComparisonResultViewModel = hiltViewModel(),
    onNavigateToProvider: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onOpenAffiliate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(fromCurrency, toCurrency, amount) {
        viewModel.loadComparison(fromCurrency, toCurrency, amount)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.comparison_results),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "${amount.formatCurrency(fromCurrency)} → PHP",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                LoadingState(
                    message = stringResource(R.string.scanning_providers),
                    modifier = Modifier.padding(paddingValues)
                )
            }

            uiState.error != null -> {
                ErrorState(
                    message = uiState.error!!,
                    modifier = Modifier.padding(paddingValues),
                    onRetry = { viewModel.loadComparison(fromCurrency, toCurrency, amount) }
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // AI Recommendation Banner
                    uiState.comparisonSummary?.bestProvider?.let { bestProvider ->
                        item {
                            AIRecommendationBanner(
                                bestProvider = bestProvider,
                                savings = uiState.comparisonSummary?.savingsVsBest ?: 0.0
                            )
                        }
                    }

                    // Results
                    itemsIndexed(uiState.results) { index, result ->
                        ProviderResultCard(
                            result = result,
                            rank = index + 1,
                            isBest = index == 0,
                            onClick = { onNavigateToProvider(result.provider.name) },
                            onSendClick = { onOpenAffiliate(result.affiliateUrl) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AIRecommendationBanner(
    bestProvider: com.aipadala.android.data.model.ComparisonResult,
    savings: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AIPadalaColors.Accent50
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.SmartToy,
                contentDescription = null,
                tint = AIPadalaColors.Accent500
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.ai_recommends),
                    style = MaterialTheme.typography.labelMedium,
                    color = AIPadalaColors.Accent600
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${bestProvider.provider.displayName} offers the best value",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                if (savings > 0) {
                    Text(
                        text = "Save ₱${savings.formatWithCommas()} vs other providers",
                        style = MaterialTheme.typography.bodySmall,
                        color = AIPadalaColors.Success600
                    )
                }
            }
        }
    }
}
