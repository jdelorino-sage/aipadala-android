package com.aipadala.android.presentation.screens.alerts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aipadala.android.R
import com.aipadala.android.data.model.AlertType
import com.aipadala.android.data.model.SupportedCurrencies
import com.aipadala.android.presentation.components.currency.CurrencySelectorBottomSheet
import com.aipadala.android.presentation.screens.comparison.CurrencyCard
import com.aipadala.android.presentation.theme.AIPadalaColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlertScreen(
    viewModel: CreateAlertViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onAlertCreated: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.create_alert)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
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
            // Currency Selection
            Text(
                text = stringResource(R.string.select_corridor),
                style = MaterialTheme.typography.titleMedium
            )

            CurrencyCard(
                currency = uiState.fromCurrency,
                label = stringResource(R.string.from),
                onClick = { viewModel.showCurrencySelector(true) }
            )

            // Alert Type
            Text(
                text = stringResource(R.string.alert_when),
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = uiState.alertType == AlertType.ABOVE,
                    onClick = { viewModel.setAlertType(AlertType.ABOVE) },
                    label = { Text(stringResource(R.string.rate_above)) },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = uiState.alertType == AlertType.BELOW,
                    onClick = { viewModel.setAlertType(AlertType.BELOW) },
                    label = { Text(stringResource(R.string.rate_below)) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Threshold Input
            OutlinedTextField(
                value = uiState.threshold,
                onValueChange = { viewModel.setThreshold(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.target_rate)) },
                placeholder = { Text("e.g., 56.50") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Create Button
            Button(
                onClick = {
                    viewModel.createAlert()
                    onAlertCreated()
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
                Text(
                    text = stringResource(R.string.create_alert),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

    if (uiState.showCurrencySelector) {
        CurrencySelectorBottomSheet(
            currencies = SupportedCurrencies.SOURCE_CURRENCIES,
            selectedCurrency = uiState.fromCurrency,
            onCurrencySelected = {
                viewModel.setFromCurrency(it)
                viewModel.showCurrencySelector(false)
            },
            onDismiss = { viewModel.showCurrencySelector(false) }
        )
    }
}
