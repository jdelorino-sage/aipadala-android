package com.aipadala.android.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aipadala.android.R
import com.aipadala.android.presentation.components.common.AIPadalaTopBar
import com.aipadala.android.presentation.components.glassmorphism.GlassGradientBackground
import com.aipadala.android.presentation.theme.AIPadalaColors

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToCompare: () -> Unit,
    onNavigateToAlerts: () -> Unit,
    onNavigateToProvider: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AIPadalaTopBar(
                title = stringResource(R.string.app_name),
                showLogo = true
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Section with Quick Compare
            item {
                HeroSection(
                    onCompareClick = onNavigateToCompare
                )
            }

            // Favorite Corridors (if any)
            if (uiState.favoriteCorridors.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.your_corridors),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                items(uiState.favoriteCorridors) { corridor ->
                    FavoriteCorridorCard(
                        corridor = corridor,
                        onClick = { /* Navigate to comparison */ }
                    )
                }
            }

            // Quick Actions
            item {
                QuickActionsRow(
                    onCompareClick = onNavigateToCompare,
                    onAlertsClick = onNavigateToAlerts
                )
            }

            // OFW Tips (Rotating)
            item {
                OFWTipCard(tip = uiState.dailyTip)
            }
        }
    }
}

@Composable
private fun HeroSection(
    onCompareClick: () -> Unit
) {
    GlassGradientBackground(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Trust Badge
            Surface(
                color = AIPadalaColors.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "⭐ ${stringResource(R.string.trusted_by_ofw_families)}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = AIPadalaColors.White
                )
            }

            // Tagalog Headline
            Column {
                Text(
                    text = stringResource(R.string.hero_headline_tagalog),
                    style = MaterialTheme.typography.headlineMedium,
                    color = AIPadalaColors.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.hero_subtext_tagalog),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AIPadalaColors.White.copy(alpha = 0.9f)
                )
            }

            // CTA Button
            Button(
                onClick = onCompareClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AIPadalaColors.CTAGradientStart
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Compare,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.compare_rates_now),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun QuickActionsRow(
    onCompareClick: () -> Unit,
    onAlertsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onCompareClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Compare, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.compare))
        }

        OutlinedButton(
            onClick = onAlertsClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Notifications, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.set_alert))
        }
    }
}

@Composable
private fun FavoriteCorridorCard(
    corridor: com.aipadala.android.data.model.FavoriteCorridor,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${corridor.fromCurrency.flagEmoji} → ${corridor.toCurrency.flagEmoji}",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "${corridor.fromCurrency.code} → ${corridor.toCurrency.code}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    corridor.currentRate?.let { rate ->
                        Text(
                            text = "1 ${corridor.fromCurrency.code} = $rate ${corridor.toCurrency.code}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OFWTipCard(tip: String?) {
    if (tip != null) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = AIPadalaColors.Primary50
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "💡 ${stringResource(R.string.tip_of_the_day)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = AIPadalaColors.Primary700
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = tip,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AIPadalaColors.Primary900
                )
            }
        }
    }
}
