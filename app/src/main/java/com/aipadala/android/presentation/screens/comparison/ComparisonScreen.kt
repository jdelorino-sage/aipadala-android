package com.aipadala.android.presentation.screens.comparison

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aipadala.android.R
import com.aipadala.android.data.model.SupportedCurrencies
import com.aipadala.android.presentation.components.currency.CurrencySelectorBottomSheet
import com.aipadala.android.presentation.theme.AIPadalaColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparisonScreen(
    viewModel: ComparisonViewModel = hiltViewModel(),
    onNavigateToResult: (String, String, Double) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.compare_rates)) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Amount Input
            AmountInputSection(
                amount = uiState.amount,
                onAmountChange = viewModel::updateAmount,
                fromCurrency = uiState.fromCurrency
            )

            // Currency Selection
            CurrencySelectionSection(
                fromCurrency = uiState.fromCurrency,
                onFromCurrencyClick = { viewModel.showCurrencySelector(true) }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Compare Button
            Button(
                onClick = {
                    onNavigateToResult(
                        uiState.fromCurrency.code,
                        uiState.toCurrency.code,
                        uiState.amount
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = uiState.isValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AIPadalaColors.Primary500
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(24.dp),
                        color = AIPadalaColors.White
                    )
                } else {
                    Icon(Icons.Default.Search, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.find_best_rate),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }

    // Currency Selector Bottom Sheet
    if (uiState.showCurrencySelector) {
        CurrencySelectorBottomSheet(
            currencies = SupportedCurrencies.SOURCE_CURRENCIES,
            selectedCurrency = uiState.fromCurrency,
            onCurrencySelected = {
                viewModel.updateFromCurrency(it)
                viewModel.showCurrencySelector(false)
            },
            onDismiss = { viewModel.showCurrencySelector(false) }
        )
    }
}

@Composable
fun AmountInputSection(
    amount: Double,
    onAmountChange: (Double) -> Unit,
    fromCurrency: com.aipadala.android.data.model.Currency
) {
    Column {
        Text(
            text = stringResource(R.string.you_send),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = if (amount > 0) amount.toString() else "",
            onValueChange = { value ->
                value.toDoubleOrNull()?.let { onAmountChange(it) }
                    ?: if (value.isEmpty()) onAmountChange(0.0) else Unit
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.headlineMedium,
            placeholder = {
                Text(
                    text = "500",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            },
            leadingIcon = {
                Text(
                    text = "${fromCurrency.flagEmoji} ${fromCurrency.symbol}",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
fun CurrencySelectionSection(
    fromCurrency: com.aipadala.android.data.model.Currency,
    onFromCurrencyClick: () -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.sending_from),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // From Currency
            CurrencyCard(
                currency = fromCurrency,
                label = stringResource(R.string.from),
                onClick = onFromCurrencyClick,
                modifier = Modifier.weight(1f)
            )

            // To Currency (Fixed PHP)
            CurrencyCard(
                currency = SupportedCurrencies.PHP,
                label = stringResource(R.string.to),
                onClick = { /* Fixed to PHP */ },
                isFixed = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CurrencyCard(
    currency: com.aipadala.android.data.model.Currency,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFixed: Boolean = false
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isFixed) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = currency.flagEmoji,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = currency.code,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                if (!isFixed) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
