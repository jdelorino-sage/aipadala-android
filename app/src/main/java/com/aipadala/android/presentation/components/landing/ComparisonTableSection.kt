package com.aipadala.android.presentation.components.landing

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aipadala.android.R
import com.aipadala.android.data.model.FeatureComparison
import com.aipadala.android.presentation.theme.AIPadalaColors

@Composable
fun ComparisonTableSection(
    comparisons: List<FeatureComparison>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AIPadalaColors.White)
            .padding(vertical = 48.dp, horizontal = 20.dp)
    ) {
        // Section Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Eyebrow text
            Surface(
                color = AIPadalaColors.Accent100,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.landing_comparison_eyebrow),
                    style = MaterialTheme.typography.labelMedium,
                    color = AIPadalaColors.Accent700,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = stringResource(R.string.landing_comparison_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AIPadalaColors.Gray900,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Comparison Table Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = AIPadalaColors.White
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Table Header
                TableHeader()

                // Table Rows
                comparisons.forEachIndexed { index, comparison ->
                    ComparisonRow(
                        comparison = comparison,
                        isEven = index % 2 == 0
                    )
                }
            }
        }
    }
}

@Composable
private fun TableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        AIPadalaColors.Primary500,
                        AIPadalaColors.Accent500
                    )
                )
            )
            .padding(vertical = 16.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.landing_feature),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = AIPadalaColors.White,
            modifier = Modifier.weight(1.2f)
        )
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = AIPadalaColors.White,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.landing_others),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = AIPadalaColors.White,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ComparisonRow(
    comparison: FeatureComparison,
    isEven: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isEven) AIPadalaColors.Gray50 else AIPadalaColors.White)
            .padding(vertical = 14.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Feature Name
        Text(
            text = comparison.feature,
            style = MaterialTheme.typography.bodyMedium,
            color = AIPadalaColors.Gray800,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1.2f)
        )

        // AI Padala Value
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (comparison.aiPadalaValue == "✓") {
                FeatureCheckmark(isPositive = true)
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (comparison.aiPadalaHasIt) {
                        FeatureCheckmark(isPositive = true)
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = comparison.aiPadalaValue,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (comparison.aiPadalaHasIt) AIPadalaColors.Success600 else AIPadalaColors.Gray600,
                        fontWeight = if (comparison.aiPadalaHasIt) FontWeight.SemiBold else FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Others Value
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (comparison.othersValue == "✗") {
                FeatureCheckmark(isPositive = false)
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (!comparison.othersHasIt) {
                        FeatureCheckmark(isPositive = false)
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = comparison.othersValue,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (comparison.othersHasIt) AIPadalaColors.Gray600 else AIPadalaColors.Error500,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureCheckmark(isPositive: Boolean) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(
                if (isPositive) AIPadalaColors.Success100 else AIPadalaColors.Error100
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isPositive) Icons.Default.Check else Icons.Default.Close,
            contentDescription = if (isPositive) "Yes" else "No",
            tint = if (isPositive) AIPadalaColors.Success600 else AIPadalaColors.Error500,
            modifier = Modifier.size(16.dp)
        )
    }
}
