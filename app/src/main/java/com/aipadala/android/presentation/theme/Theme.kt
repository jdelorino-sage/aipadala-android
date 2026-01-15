package com.aipadala.android.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Apple-inspired Light Color Scheme
 * Clean, minimal with purposeful color use
 */
private val LightColorScheme = lightColorScheme(
    primary = AIPadalaColors.Primary500,           // Apple Blue
    onPrimary = AIPadalaColors.White,
    primaryContainer = AIPadalaColors.Primary50,
    onPrimaryContainer = AIPadalaColors.Primary900,

    secondary = AIPadalaColors.Accent500,          // Apple Indigo
    onSecondary = AIPadalaColors.White,
    secondaryContainer = AIPadalaColors.Accent50,
    onSecondaryContainer = AIPadalaColors.Accent900,

    tertiary = AIPadalaColors.Success500,          // Apple Green
    onTertiary = AIPadalaColors.White,
    tertiaryContainer = AIPadalaColors.Success50,
    onTertiaryContainer = AIPadalaColors.Success700,

    error = AIPadalaColors.Error500,               // Apple Red
    onError = AIPadalaColors.White,
    errorContainer = AIPadalaColors.Error50,
    onErrorContainer = AIPadalaColors.Error600,

    background = AIPadalaColors.SystemBackground,  // iOS System Gray 6
    onBackground = AIPadalaColors.Gray900,

    surface = AIPadalaColors.White,                // Clean white cards
    onSurface = AIPadalaColors.Gray900,
    surfaceVariant = AIPadalaColors.Gray100,
    onSurfaceVariant = AIPadalaColors.Gray500,     // iOS Secondary Label

    outline = AIPadalaColors.Gray300,
    outlineVariant = AIPadalaColors.Gray200
)

/**
 * Apple-inspired Dark Color Scheme
 * True blacks with subtle depth like iOS dark mode
 */
private val DarkColorScheme = darkColorScheme(
    primary = AIPadalaColors.Primary400,           // Lighter blue for dark mode
    onPrimary = AIPadalaColors.Primary950,
    primaryContainer = AIPadalaColors.Primary900,
    onPrimaryContainer = AIPadalaColors.Primary100,

    secondary = AIPadalaColors.Accent400,
    onSecondary = AIPadalaColors.Accent950,
    secondaryContainer = AIPadalaColors.Accent900,
    onSecondaryContainer = AIPadalaColors.Accent100,

    tertiary = AIPadalaColors.Success500,
    onTertiary = AIPadalaColors.White,
    tertiaryContainer = AIPadalaColors.Success700,
    onTertiaryContainer = AIPadalaColors.Success100,

    error = AIPadalaColors.Error500,
    onError = AIPadalaColors.White,
    errorContainer = AIPadalaColors.Error600,
    onErrorContainer = AIPadalaColors.Error100,

    background = AIPadalaColors.DarkSystemBackground,    // True black (iOS style)
    onBackground = AIPadalaColors.White,

    surface = AIPadalaColors.DarkSecondaryBackground,    // Elevated dark surface
    onSurface = AIPadalaColors.White,
    surfaceVariant = AIPadalaColors.DarkTertiaryBackground,
    onSurfaceVariant = AIPadalaColors.Gray400,

    outline = AIPadalaColors.Gray600,
    outlineVariant = AIPadalaColors.Gray700
)

@Composable
fun AIPadalaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled to maintain brand consistency
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Apple-style: transparent status bar with content color
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AIPadalaTypography,
        shapes = AIPadalaShapes,
        content = content
    )
}
