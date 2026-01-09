package com.aipadala.android.presentation.components.landing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aipadala.android.R
import com.aipadala.android.data.model.Currency
import com.aipadala.android.data.model.LandingStats
import com.aipadala.android.data.model.QuickCompareResult
import com.aipadala.android.data.model.SupportedCurrencies
import com.aipadala.android.presentation.theme.AIPadalaColors

@Composable
fun HeroSection(
    stats: LandingStats,
    sendAmount: Double,
    selectedCurrency: Currency,
    isComparing: Boolean,
    compareResults: List<QuickCompareResult>,
    showCompareResults: Boolean,
    onAmountChange: (Double) -> Unit,
    onCurrencySelect: (Currency) -> Unit,
    onCompareClick: () -> Unit,
    onProviderClick: (QuickCompareResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AIPadalaColors.HeroGradient1,
                        AIPadalaColors.HeroGradient2,
                        AIPadalaColors.HeroGradient3,
                        AIPadalaColors.HeroGradient4,
                        AIPadalaColors.HeroGradient5
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Trust Badge
            TrustBadge()

            Spacer(modifier = Modifier.height(24.dp))

            // Headline (Tagalog - Emotional)
            Text(
                text = stringResource(R.string.hero_headline_tagalog),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = AIPadalaColors.White,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subheadline
            Text(
                text = stringResource(R.string.hero_subtext_tagalog),
                style = MaterialTheme.typography.bodyLarge,
                color = AIPadalaColors.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Quick Compare Widget
            QuickCompareWidget(
                sendAmount = sendAmount,
                selectedCurrency = selectedCurrency,
                isComparing = isComparing,
                onAmountChange = onAmountChange,
                onCurrencySelect = onCurrencySelect,
                onCompareClick = onCompareClick
            )

            // Compare Results
            AnimatedVisibility(
                visible = showCompareResults && compareResults.isNotEmpty(),
                enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
            ) {
                CompareResultsDisplay(
                    results = compareResults,
                    selectedCurrency = selectedCurrency,
                    onProviderClick = onProviderClick,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Social Proof Stats
            SocialProofStats(stats = stats)
        }
    }
}

@Composable
private fun TrustBadge() {
    Surface(
        color = AIPadalaColors.White.copy(alpha = 0.15f),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = AIPadalaColors.Warning500,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = stringResource(R.string.trusted_by_ofw_families),
                style = MaterialTheme.typography.labelMedium,
                color = AIPadalaColors.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun QuickCompareWidget(
    sendAmount: Double,
    selectedCurrency: Currency,
    isComparing: Boolean,
    onAmountChange: (Double) -> Unit,
    onCurrencySelect: (Currency) -> Unit,
    onCompareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var amountText by remember { mutableStateOf(sendAmount.toInt().toString()) }
    var showCurrencyDropdown by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AIPadalaColors.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.you_send),
                style = MaterialTheme.typography.labelMedium,
                color = AIPadalaColors.Gray500
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Currency Selector
                Box {
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { showCurrencyDropdown = true },
                        color = AIPadalaColors.Gray100,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = selectedCurrency.flagEmoji,
                                fontSize = 20.sp
                            )
                            Text(
                                text = selectedCurrency.code,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = AIPadalaColors.Gray900
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Select currency",
                                tint = AIPadalaColors.Gray500,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showCurrencyDropdown,
                        onDismissRequest = { showCurrencyDropdown = false }
                    ) {
                        // Popular currencies first
                        Text(
                            text = stringResource(R.string.popular),
                            style = MaterialTheme.typography.labelSmall,
                            color = AIPadalaColors.Gray500,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        SupportedCurrencies.POPULAR_CURRENCIES.forEach { currency ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(text = currency.flagEmoji, fontSize = 18.sp)
                                        Text(
                                            text = "${currency.code} - ${currency.name}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                },
                                onClick = {
                                    onCurrencySelect(currency)
                                    showCurrencyDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Amount Input
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { newValue ->
                        // Allow only numeric input
                        val filtered = newValue.filter { it.isDigit() || it == '.' }
                        amountText = filtered
                        filtered.toDoubleOrNull()?.let { onAmountChange(it) }
                    },
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AIPadalaColors.Gray900
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AIPadalaColors.Primary500,
                        unfocusedBorderColor = AIPadalaColors.Gray200
                    ),
                    shape = RoundedCornerShape(12.dp),
                    prefix = {
                        Text(
                            text = selectedCurrency.symbol,
                            style = MaterialTheme.typography.titleMedium,
                            color = AIPadalaColors.Gray500
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Compare Button
            val buttonScale by animateFloatAsState(
                targetValue = if (isComparing) 0.98f else 1f,
                animationSpec = tween(150),
                label = "button_scale"
            )

            Button(
                onClick = onCompareClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .scale(buttonScale),
                enabled = !isComparing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AIPadalaColors.CTAGradientStart,
                    disabledContainerColor = AIPadalaColors.CTAGradientStart.copy(alpha = 0.7f)
                ),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 2.dp
                )
            ) {
                if (isComparing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = AIPadalaColors.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.scanning_providers),
                        color = AIPadalaColors.White,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = AIPadalaColors.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.compare_rates_now),
                        color = AIPadalaColors.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun CompareResultsDisplay(
    results: List<QuickCompareResult>,
    selectedCurrency: Currency,
    onProviderClick: (QuickCompareResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AIPadalaColors.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.comparison_results),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AIPadalaColors.Gray900
            )

            results.take(4).forEachIndexed { index, result ->
                ProviderResultCard(
                    result = result,
                    currencyCode = selectedCurrency.code,
                    onClick = { onProviderClick(result) },
                    animationDelay = index * 100
                )
            }
        }
    }
}

@Composable
private fun ProviderResultCard(
    result: QuickCompareResult,
    currencyCode: String,
    onClick: () -> Unit,
    animationDelay: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (result.isBestRate) AIPadalaColors.Success50 else AIPadalaColors.Gray50
        ),
        shape = RoundedCornerShape(12.dp),
        border = if (result.isBestRate) {
            androidx.compose.foundation.BorderStroke(2.dp, AIPadalaColors.Success500)
        } else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Provider Logo Placeholder
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(AIPadalaColors.Primary100),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = result.providerName.first().toString(),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = AIPadalaColors.Primary700
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = result.providerName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = AIPadalaColors.Gray900
                            )
                            if (result.isBestRate) {
                                Surface(
                                    color = AIPadalaColors.Success500,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.best_value),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AIPadalaColors.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "1 $currencyCode = ₱${String.format("%.2f", result.exchangeRate)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AIPadalaColors.Gray500
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "₱${String.format("%,.2f", result.recipientGets)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (result.isBestRate) AIPadalaColors.Success600 else AIPadalaColors.Gray900
                    )
                    Text(
                        text = if (result.transferFee == 0.0) "No fee" else "Fee: ₱${String.format("%.2f", result.transferFee)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AIPadalaColors.Gray500
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "⚡ ${result.transferSpeed}",
                style = MaterialTheme.typography.labelSmall,
                color = AIPadalaColors.Primary600
            )
        }
    }
}

@Composable
private fun SocialProofStats(
    stats: LandingStats,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(
            value = stats.totalSaved,
            label = stringResource(R.string.landing_saved_by_families),
            emoji = "💰"
        )
        StatItem(
            value = stats.ofwFamilies,
            label = stringResource(R.string.landing_active_users),
            emoji = "👨‍👩‍👧‍👦"
        )
        StatItem(
            value = "5+",
            label = stringResource(R.string.landing_providers_compared),
            emoji = "🏆"
        )
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    emoji: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = emoji,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AIPadalaColors.White
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = AIPadalaColors.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}
