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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = AIPadalaColors.Primary500,
    onPrimary = AIPadalaColors.White,
    primaryContainer = AIPadalaColors.Primary100,
    onPrimaryContainer = AIPadalaColors.Primary900,

    secondary = AIPadalaColors.Accent500,
    onSecondary = AIPadalaColors.White,
    secondaryContainer = AIPadalaColors.Accent100,
    onSecondaryContainer = AIPadalaColors.Accent900,

    tertiary = AIPadalaColors.Success500,
    onTertiary = AIPadalaColors.White,
    tertiaryContainer = AIPadalaColors.Success100,
    onTertiaryContainer = AIPadalaColors.Success700,

    error = AIPadalaColors.Error500,
    onError = AIPadalaColors.White,
    errorContainer = AIPadalaColors.Error100,
    onErrorContainer = AIPadalaColors.Error600,

    background = AIPadalaColors.Gray50,
    onBackground = AIPadalaColors.Gray900,

    surface = AIPadalaColors.White,
    onSurface = AIPadalaColors.Gray900,
    surfaceVariant = AIPadalaColors.Gray100,
    onSurfaceVariant = AIPadalaColors.Gray700,

    outline = AIPadalaColors.Gray300,
    outlineVariant = AIPadalaColors.Gray200
)

private val DarkColorScheme = darkColorScheme(
    primary = AIPadalaColors.Primary400,
    onPrimary = AIPadalaColors.Primary950,
    primaryContainer = AIPadalaColors.Primary800,
    onPrimaryContainer = AIPadalaColors.Primary100,

    secondary = AIPadalaColors.Accent400,
    onSecondary = AIPadalaColors.Accent950,
    secondaryContainer = AIPadalaColors.Accent800,
    onSecondaryContainer = AIPadalaColors.Accent100,

    tertiary = AIPadalaColors.Success500,
    onTertiary = AIPadalaColors.White,
    tertiaryContainer = AIPadalaColors.Success700,
    onTertiaryContainer = AIPadalaColors.Success100,

    error = AIPadalaColors.Error500,
    onError = AIPadalaColors.White,
    errorContainer = AIPadalaColors.Error600,
    onErrorContainer = AIPadalaColors.Error100,

    background = AIPadalaColors.Gray900,
    onBackground = AIPadalaColors.Gray100,

    surface = AIPadalaColors.Gray800,
    onSurface = AIPadalaColors.Gray100,
    surfaceVariant = AIPadalaColors.Gray700,
    onSurfaceVariant = AIPadalaColors.Gray300,

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
            window.statusBarColor = colorScheme.primary.toArgb()
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
