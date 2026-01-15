package com.aipadala.android.presentation.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aipadala.android.R
import com.aipadala.android.presentation.components.apple.AppleFadeSlideIn
import com.aipadala.android.presentation.components.apple.AppleAnimations
import com.aipadala.android.presentation.components.apple.ApplePrimaryButton
import com.aipadala.android.presentation.components.apple.AppleStyleCard
import com.aipadala.android.presentation.components.apple.AppleStyleSubtleCard
import com.aipadala.android.presentation.components.apple.QuickActionTile
import com.aipadala.android.presentation.theme.AIPadalaColors
import com.aipadala.android.presentation.theme.Dimensions
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToCompare: () -> Unit,
    onNavigateToAlerts: () -> Unit,
    onNavigateToProvider: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Animation state
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        showContent = true
    }

    Scaffold(
        containerColor = AIPadalaColors.SystemBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                start = Dimensions.PaddingScreen,
                end = Dimensions.PaddingScreen,
                top = Dimensions.Spacing3xl,
                bottom = Dimensions.Spacing4xl
            ),
            verticalArrangement = Arrangement.spacedBy(Dimensions.PaddingSection)
        ) {
            // Hero Section - Clean, minimal
            item {
                AppleFadeSlideIn(visible = showContent, delayMillis = 0) {
                    HeroSection(onCompareClick = onNavigateToCompare)
                }
            }

            // Favorite Corridors (if any)
            if (uiState.favoriteCorridors.isNotEmpty()) {
                item {
                    AppleFadeSlideIn(
                        visible = showContent,
                        delayMillis = AppleAnimations.STAGGER_DELAY * 2
                    ) {
                        SectionHeader(title = stringResource(R.string.your_corridors))
                    }
                }
                itemsIndexed(uiState.favoriteCorridors) { index, corridor ->
                    AppleFadeSlideIn(
                        visible = showContent,
                        delayMillis = AppleAnimations.STAGGER_DELAY * (3 + index)
                    ) {
                        FavoriteCorridorCard(
                            corridor = corridor,
                            onClick = { /* Navigate to comparison */ }
                        )
                    }
                }
            }

            // Quick Actions
            item {
                AppleFadeSlideIn(
                    visible = showContent,
                    delayMillis = AppleAnimations.STAGGER_DELAY * 5
                ) {
                    Column {
                        SectionHeader(title = stringResource(R.string.quick_actions))
                        Spacer(modifier = Modifier.height(Dimensions.SpacingMd))
                        QuickActionsRow(
                            onCompareClick = onNavigateToCompare,
                            onAlertsClick = onNavigateToAlerts
                        )
                    }
                }
            }

            // OFW Tip
            item {
                AppleFadeSlideIn(
                    visible = showContent,
                    delayMillis = AppleAnimations.STAGGER_DELAY * 6
                ) {
                    OFWTipBanner(tip = uiState.dailyTip)
                }
            }
        }
    }
}

@Composable
private fun HeroSection(
    onCompareClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        // Large headline (Apple-style)
        Text(
            text = stringResource(R.string.hero_headline_english),
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = 34.sp,
                lineHeight = 41.sp,
                letterSpacing = 0.25.sp
            ),
            fontWeight = FontWeight.Bold,
            color = AIPadalaColors.Gray900
        )

        Spacer(modifier = Modifier.height(Dimensions.SpacingMd))

        // Subtext
        Text(
            text = stringResource(R.string.hero_subtext_english),
            style = MaterialTheme.typography.bodyLarge,
            color = AIPadalaColors.Gray500,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(Dimensions.SpacingSm))

        // Tagalog tagline (emotional connection)
        Text(
            text = stringResource(R.string.hero_tagline_tagalog),
            style = MaterialTheme.typography.bodyMedium,
            color = AIPadalaColors.Primary500,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(Dimensions.SpacingXxl))

        // CTA Button
        ApplePrimaryButton(
            text = stringResource(R.string.compare_rates_now),
            onClick = onCompareClick,
            showArrow = true
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontSize = 20.sp,
            letterSpacing = 0.15.sp
        ),
        fontWeight = FontWeight.SemiBold,
        color = AIPadalaColors.Gray900,
        modifier = modifier
    )
}

@Composable
private fun QuickActionsRow(
    onCompareClick: () -> Unit,
    onAlertsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.SpacingLg)
    ) {
        QuickActionTile(
            icon = Icons.Outlined.CompareArrows,
            label = stringResource(R.string.compare),
            onClick = onCompareClick,
            iconTint = AIPadalaColors.Primary500,
            modifier = Modifier.weight(1f)
        )

        QuickActionTile(
            icon = Icons.Outlined.Notifications,
            label = stringResource(R.string.set_alert),
            onClick = onAlertsClick,
            iconTint = AIPadalaColors.Warning500,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun FavoriteCorridorCard(
    corridor: com.aipadala.android.data.model.FavoriteCorridor,
    onClick: () -> Unit
) {
    AppleStyleCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Flag emojis
                Text(
                    text = "${corridor.fromCurrency.flagEmoji} → ${corridor.toCurrency.flagEmoji}",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.width(Dimensions.SpacingMd))

                Column {
                    Text(
                        text = "${corridor.fromCurrency.code} → ${corridor.toCurrency.code}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AIPadalaColors.Gray900
                    )
                    corridor.currentRate?.let { rate ->
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "1 ${corridor.fromCurrency.code} = $rate ${corridor.toCurrency.code}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AIPadalaColors.Gray500
                        )
                    }
                }
            }

            // Trend indicator
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = AIPadalaColors.Success500.copy(alpha = 0.1f),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Outlined.TrendingUp,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = AIPadalaColors.Success500
                )
            }
        }
    }
}

@Composable
private fun OFWTipBanner(tip: String?) {
    if (tip != null) {
        AppleStyleSubtleCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = AIPadalaColors.Primary50
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "💡",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.width(Dimensions.SpacingMd))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.tip_of_the_day),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AIPadalaColors.Primary600
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = tip,
                        style = MaterialTheme.typography.bodySmall,
                        color = AIPadalaColors.Primary900,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
