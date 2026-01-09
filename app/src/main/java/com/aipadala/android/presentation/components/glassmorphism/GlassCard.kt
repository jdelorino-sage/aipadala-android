package com.aipadala.android.presentation.components.glassmorphism

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aipadala.android.presentation.theme.AIPadalaColors

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    backgroundColor: Color = AIPadalaColors.GlassBg,
    borderColor: Color = AIPadalaColors.GlassBorder,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)

    Card(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(content = content)
    }
}

@Composable
fun GlassGradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(
                Brush.linearGradient(
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
        content()
    }
}

@Composable
fun GlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .background(
                if (enabled) {
                    AIPadalaColors.GlassBg
                } else {
                    AIPadalaColors.GlassBg.copy(alpha = 0.3f)
                }
            )
            .border(
                width = 1.dp,
                color = if (enabled) {
                    AIPadalaColors.GlassBorder
                } else {
                    AIPadalaColors.GlassBorder.copy(alpha = 0.3f)
                },
                shape = shape
            )
    ) {
        content()
    }
}
