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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aipadala.android.R
import com.aipadala.android.presentation.theme.AIPadalaColors
import kotlinx.coroutines.delay

data class HowItWorksStepData(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val icon: ImageVector
)

@Composable
fun HowItWorksSection(
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        HowItWorksStepData(
            stepNumber = 1,
            title = stringResource(R.string.landing_step1_title),
            description = stringResource(R.string.landing_step1_desc),
            icon = Icons.Default.PersonAdd
        ),
        HowItWorksStepData(
            stepNumber = 2,
            title = stringResource(R.string.landing_step2_title),
            description = stringResource(R.string.landing_step2_desc),
            icon = Icons.Default.Calculate
        ),
        HowItWorksStepData(
            stepNumber = 3,
            title = stringResource(R.string.landing_step3_title),
            description = stringResource(R.string.landing_step3_desc),
            icon = Icons.AutoMirrored.Filled.Send
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AIPadalaColors.Primary50,
                        AIPadalaColors.White
                    )
                )
            )
            .padding(vertical = 48.dp, horizontal = 20.dp)
    ) {
        // Section Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Eyebrow text
            Surface(
                color = AIPadalaColors.Primary100,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.landing_how_it_works_eyebrow),
                    style = MaterialTheme.typography.labelMedium,
                    color = AIPadalaColors.Primary700,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = stringResource(R.string.landing_how_it_works),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AIPadalaColors.Gray900,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = stringResource(R.string.landing_how_it_works_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = AIPadalaColors.Gray600,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Steps - Vertical Layout for Mobile
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            steps.forEachIndexed { index, step ->
                StepCard(
                    step = step,
                    isLast = index == steps.lastIndex,
                    animationDelay = index * 150
                )
            }
        }
    }
}

@Composable
private fun StepCard(
    step: HowItWorksStepData,
    isLast: Boolean,
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
        label = "step_scale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        verticalAlignment = Alignment.Top
    ) {
        // Step Number and Connector
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Number Circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                AIPadalaColors.Primary500,
                                AIPadalaColors.Accent500
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = step.stepNumber.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AIPadalaColors.White
                )
            }

            // Connector Line
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(80.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    AIPadalaColors.Primary300,
                                    AIPadalaColors.Primary100
                                )
                            )
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Step Content Card
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = AIPadalaColors.White
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Icon
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AIPadalaColors.Primary100),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = step.icon,
                            contentDescription = null,
                            tint = AIPadalaColors.Primary600,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = step.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AIPadalaColors.Gray900
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = step.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AIPadalaColors.Gray600,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
