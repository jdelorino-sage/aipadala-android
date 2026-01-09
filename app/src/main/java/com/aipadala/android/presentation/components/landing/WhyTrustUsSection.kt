package com.aipadala.android.presentation.components.landing

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aipadala.android.R
import com.aipadala.android.presentation.theme.AIPadalaColors
import kotlinx.coroutines.delay

data class TrustPillarData(
    val icon: String,
    val title: String,
    val description: String,
    val iconBackgroundColor: Color
)

@Composable
fun WhyTrustUsSection(
    modifier: Modifier = Modifier
) {
    val trustPillars = listOf(
        TrustPillarData(
            icon = "🔒",
            title = stringResource(R.string.landing_trust_security_title),
            description = stringResource(R.string.landing_trust_security_desc),
            iconBackgroundColor = AIPadalaColors.Primary100
        ),
        TrustPillarData(
            icon = "⚡",
            title = stringResource(R.string.landing_trust_speed_title),
            description = stringResource(R.string.landing_trust_speed_desc),
            iconBackgroundColor = AIPadalaColors.Warning100
        ),
        TrustPillarData(
            icon = "💰",
            title = stringResource(R.string.landing_trust_rates_title),
            description = stringResource(R.string.landing_trust_rates_desc),
            iconBackgroundColor = AIPadalaColors.Success100
        ),
        TrustPillarData(
            icon = "💬",
            title = stringResource(R.string.landing_trust_support_title),
            description = stringResource(R.string.landing_trust_support_desc),
            iconBackgroundColor = AIPadalaColors.Accent100
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AIPadalaColors.Gray50)
            .padding(vertical = 48.dp, horizontal = 20.dp)
    ) {
        // Section Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Eyebrow text
            Surface(
                color = AIPadalaColors.Success100,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.landing_trust_eyebrow),
                    style = MaterialTheme.typography.labelMedium,
                    color = AIPadalaColors.Success700,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = stringResource(R.string.landing_why_trust_us),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AIPadalaColors.Gray900,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = stringResource(R.string.landing_trust_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = AIPadalaColors.Gray600,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Trust Pillars Grid (2x2 on mobile)
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TrustPillarCard(
                    pillar = trustPillars[0],
                    modifier = Modifier.weight(1f),
                    animationDelay = 0
                )
                TrustPillarCard(
                    pillar = trustPillars[1],
                    modifier = Modifier.weight(1f),
                    animationDelay = 100
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TrustPillarCard(
                    pillar = trustPillars[2],
                    modifier = Modifier.weight(1f),
                    animationDelay = 200
                )
                TrustPillarCard(
                    pillar = trustPillars[3],
                    modifier = Modifier.weight(1f),
                    animationDelay = 300
                )
            }
        }
    }
}

@Composable
private fun TrustPillarCard(
    pillar: TrustPillarData,
    modifier: Modifier = Modifier,
    animationDelay: Int
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(animationDelay.toLong())
        isVisible = true
    }

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.9f,
        animationSpec = tween(400),
        label = "pillar_scale"
    )

    Card(
        modifier = modifier.scale(scale),
        colors = CardDefaults.cardColors(
            containerColor = AIPadalaColors.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(pillar.iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = pillar.icon,
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = pillar.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = AIPadalaColors.Gray900,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = pillar.description,
                style = MaterialTheme.typography.bodySmall,
                color = AIPadalaColors.Gray600,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}
