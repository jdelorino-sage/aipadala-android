package com.aipadala.android.presentation.components.landing

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aipadala.android.R
import com.aipadala.android.data.model.LandingStats
import com.aipadala.android.data.model.Testimonial
import com.aipadala.android.presentation.theme.AIPadalaColors
import kotlinx.coroutines.delay

@Composable
fun TestimonialsSection(
    testimonials: List<Testimonial>,
    stats: LandingStats,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AIPadalaColors.Gray50)
            .padding(vertical = 48.dp)
    ) {
        // Section Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Eyebrow text
            Surface(
                color = AIPadalaColors.Primary100,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.landing_real_stories),
                    style = MaterialTheme.typography.labelMedium,
                    color = AIPadalaColors.Primary700,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = stringResource(R.string.landing_testimonials_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AIPadalaColors.Gray900,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = stringResource(R.string.landing_testimonials_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = AIPadalaColors.Gray600,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Testimonial Cards - Horizontal Scrolling
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(testimonials) { index, testimonial ->
                TestimonialCard(
                    testimonial = testimonial,
                    index = index
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Stats Bar
        StatsBar(stats = stats)
    }
}

@Composable
private fun TestimonialCard(
    testimonial: Testimonial,
    index: Int,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * 100L)
        isVisible = true
    }

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.9f,
        animationSpec = tween(300),
        label = "card_scale"
    )

    Card(
        modifier = modifier
            .width(300.dp)
            .scale(scale),
        colors = CardDefaults.cardColors(
            containerColor = AIPadalaColors.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Quote Icon
            Icon(
                imageVector = Icons.Default.FormatQuote,
                contentDescription = null,
                tint = AIPadalaColors.Primary300,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quote Text
            Text(
                text = "\"${testimonial.quote}\"",
                style = MaterialTheme.typography.bodyMedium,
                color = AIPadalaColors.Gray700,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Author Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    AIPadalaColors.Primary400,
                                    AIPadalaColors.Accent400
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = testimonial.name.split(" ").mapNotNull { it.firstOrNull()?.uppercase() }.take(2).joinToString(""),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AIPadalaColors.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = testimonial.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AIPadalaColors.Gray900
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${testimonial.countryFlag} ${testimonial.countryName}",
                            style = MaterialTheme.typography.labelMedium,
                            color = AIPadalaColors.Gray500
                        )
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.labelMedium,
                            color = AIPadalaColors.Gray400
                        )
                        Text(
                            text = testimonial.jobTitle,
                            style = MaterialTheme.typography.labelMedium,
                            color = AIPadalaColors.Gray500
                        )
                    }
                }

                // Savings Badge
                Surface(
                    color = AIPadalaColors.Success50,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.landing_saves),
                            style = MaterialTheme.typography.labelSmall,
                            color = AIPadalaColors.Success600
                        )
                        Text(
                            text = testimonial.monthlySavings,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = AIPadalaColors.Success700
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsBar(
    stats: LandingStats,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = AIPadalaColors.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AnimatedStatItem(
                icon = Icons.Default.Groups,
                value = stats.ofwFamilies,
                label = stringResource(R.string.landing_ofw_families),
                iconColor = AIPadalaColors.Primary500
            )

            StatDivider()

            AnimatedStatItem(
                icon = Icons.Default.Savings,
                value = stats.totalSaved,
                label = stringResource(R.string.landing_total_saved),
                iconColor = AIPadalaColors.Success500
            )

            StatDivider()

            AnimatedStatItem(
                icon = Icons.Default.Star,
                value = stats.averageRating,
                label = stringResource(R.string.landing_avg_rating),
                iconColor = AIPadalaColors.Warning500,
                showStar = true
            )

            StatDivider()

            AnimatedStatItem(
                icon = Icons.Default.Public,
                value = stats.countries,
                label = stringResource(R.string.landing_countries),
                iconColor = AIPadalaColors.Accent500
            )
        }
    }
}

@Composable
private fun AnimatedStatItem(
    icon: ImageVector,
    value: String,
    label: String,
    iconColor: Color,
    showStar: Boolean = false
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = tween(500),
        label = "stat_scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.scale(scale)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = AIPadalaColors.Gray900
            )
            if (showStar) {
                Text(
                    text = "★",
                    fontSize = 16.sp,
                    color = AIPadalaColors.Warning500
                )
            }
        }

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = AIPadalaColors.Gray500,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .height(48.dp)
            .width(1.dp)
            .background(AIPadalaColors.Gray200)
    )
}
