package com.aipadala.android.presentation.components.apple

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Apple-style animation specs and composables
 * Smooth, elegant transitions inspired by iOS
 */
object AppleAnimations {

    // Duration constants (Apple typically uses 300-400ms)
    const val DURATION_FAST = 200
    const val DURATION_NORMAL = 300
    const val DURATION_SLOW = 400

    // Stagger delay for list items
    const val STAGGER_DELAY = 50

    // Spring animation specs (Apple uses spring-based animations)
    val springSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    val gentleSpringSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    )
}

/**
 * Apple-style fade + slide up entrance animation
 */
@Composable
fun AppleFadeSlideIn(
    visible: Boolean,
    modifier: Modifier = Modifier,
    delayMillis: Int = 0,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = AppleAnimations.DURATION_NORMAL,
                delayMillis = delayMillis
            )
        ) + slideInVertically(
            animationSpec = tween(
                durationMillis = AppleAnimations.DURATION_NORMAL,
                delayMillis = delayMillis
            ),
            initialOffsetY = { it / 4 } // Slide up from 25% below
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = AppleAnimations.DURATION_FAST)
        ) + slideOutVertically(
            animationSpec = tween(durationMillis = AppleAnimations.DURATION_FAST),
            targetOffsetY = { -it / 4 }
        ),
        content = content
    )
}

/**
 * Simple fade animation
 */
@Composable
fun AppleFade(
    visible: Boolean,
    modifier: Modifier = Modifier,
    delayMillis: Int = 0,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = AppleAnimations.DURATION_NORMAL,
                delayMillis = delayMillis
            )
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = AppleAnimations.DURATION_FAST)
        ),
        content = content
    )
}
