package com.aipadala.android.presentation.theme

import androidx.compose.ui.graphics.Color

/**
 * AI Padala Brand Colors - Apple-Inspired Design System
 * Primary: Apple Blue (#007AFF) - Trust, reliability, familiarity
 * Design Philosophy: Clean, minimal, elegant with purposeful color use
 */
object AIPadalaColors {

    // ═══════════════════════════════════════════════════════════════
    // PRIMARY COLORS (Apple Blue - Trust & Familiarity)
    // ═══════════════════════════════════════════════════════════════
    val Primary50 = Color(0xFFF0F7FF)   // Lightest - subtle backgrounds
    val Primary100 = Color(0xFFE0EFFF)  // Light backgrounds
    val Primary200 = Color(0xFFC2DFFF)  // Hover states
    val Primary300 = Color(0xFF94C8FF)  // Light accents
    val Primary400 = Color(0xFF5AAAFF)  // Lighter blue
    val Primary500 = Color(0xFF007AFF)  // ★ APPLE BLUE - MAIN PRIMARY
    val Primary600 = Color(0xFF0066D6)  // Pressed state
    val Primary700 = Color(0xFF0052AD)  // Dark accents
    val Primary800 = Color(0xFF003D82)  // Deep blue
    val Primary900 = Color(0xFF002957)  // Text, dark mode bg
    val Primary950 = Color(0xFF001A3D)  // Darkest

    // ═══════════════════════════════════════════════════════════════
    // ACCENT COLORS (Apple Indigo - Premium & Modern)
    // ═══════════════════════════════════════════════════════════════
    val Accent50 = Color(0xFFF5F3FF)    // Lightest
    val Accent100 = Color(0xFFEDE9FE)   // Light backgrounds
    val Accent200 = Color(0xFFDDD6FE)   // Hover states
    val Accent300 = Color(0xFFC4B5FD)   // Light accents
    val Accent400 = Color(0xFFA78BFA)   // Lighter indigo
    val Accent500 = Color(0xFF5856D6)   // ★ APPLE INDIGO
    val Accent600 = Color(0xFF4F46E5)   // Pressed state
    val Accent700 = Color(0xFF4338CA)   // Dark accents
    val Accent800 = Color(0xFF3730A3)   // Deep indigo
    val Accent900 = Color(0xFF312E81)   // Very dark
    val Accent950 = Color(0xFF1E1B4B)   // Darkest

    // ═══════════════════════════════════════════════════════════════
    // SEMANTIC COLORS (Apple System Colors)
    // ═══════════════════════════════════════════════════════════════
    val Success50 = Color(0xFFECFDF3)
    val Success100 = Color(0xFFD1FADF)
    val Success500 = Color(0xFF34C759)  // Apple Green
    val Success600 = Color(0xFF28A745)  // Darker green
    val Success700 = Color(0xFF1E7E34)

    val Warning50 = Color(0xFFFFFBEB)
    val Warning100 = Color(0xFFFEF3C7)
    val Warning500 = Color(0xFFFF9500)  // Apple Orange
    val Warning600 = Color(0xFFE68600)  // Darker orange

    val Error50 = Color(0xFFFEF2F2)
    val Error100 = Color(0xFFFEE2E2)
    val Error500 = Color(0xFFFF3B30)   // Apple Red
    val Error600 = Color(0xFFE6352B)   // Darker red

    // ═══════════════════════════════════════════════════════════════
    // APPLE SYSTEM GRAYS (iOS-inspired neutral palette)
    // ═══════════════════════════════════════════════════════════════
    val White = Color(0xFFFFFFFF)
    val Gray50 = Color(0xFFF9FAFB)     // System background
    val Gray100 = Color(0xFFF2F2F7)    // ★ iOS System Gray 6 - Main background
    val Gray200 = Color(0xFFE5E5EA)    // iOS System Gray 5
    val Gray300 = Color(0xFFD1D1D6)    // iOS System Gray 4
    val Gray400 = Color(0xFFC7C7CC)    // iOS System Gray 3
    val Gray500 = Color(0xFF8E8E93)    // ★ iOS Secondary Label
    val Gray600 = Color(0xFF636366)    // iOS System Gray 2
    val Gray700 = Color(0xFF48484A)    // iOS System Gray
    val Gray800 = Color(0xFF2C2C2E)    // Dark surface
    val Gray900 = Color(0xFF1C1C1E)    // ★ iOS Primary Label / Dark bg
    val Black = Color(0xFF000000)

    // ═══════════════════════════════════════════════════════════════
    // APPLE-STYLE BACKGROUNDS
    // ═══════════════════════════════════════════════════════════════
    val SystemBackground = Color(0xFFF2F2F7)        // iOS grouped background
    val SecondarySystemBackground = Color(0xFFFFFFFF) // Elevated cards
    val TertiarySystemBackground = Color(0xFFF2F2F7)  // Tertiary content

    // Dark mode backgrounds
    val DarkSystemBackground = Color(0xFF000000)
    val DarkSecondaryBackground = Color(0xFF1C1C1E)
    val DarkTertiaryBackground = Color(0xFF2C2C2E)

    // ═══════════════════════════════════════════════════════════════
    // APPLE-STYLE SEPARATORS & OVERLAYS
    // ═══════════════════════════════════════════════════════════════
    val Separator = Color(0x4D3C3C43)              // iOS separator (opaque)
    val SeparatorOpaque = Color(0xFFC6C6C8)       // Non-transparent separator
    val Overlay = Color(0x4D000000)               // Modal overlay

    // ═══════════════════════════════════════════════════════════════
    // SOFT SHADOW COLORS (Apple-style elevation)
    // ═══════════════════════════════════════════════════════════════
    val ShadowLight = Color(0x0A000000)           // 4% black - subtle
    val ShadowMedium = Color(0x14000000)          // 8% black - cards
    val ShadowStrong = Color(0x1F000000)          // 12% black - elevated

    // ═══════════════════════════════════════════════════════════════
    // CTA GRADIENT (Apple Blue gradient)
    // ═══════════════════════════════════════════════════════════════
    val CTAGradientStart = Color(0xFF007AFF)      // Apple Blue
    val CTAGradientEnd = Color(0xFF5856D6)        // Apple Indigo

    // ═══════════════════════════════════════════════════════════════
    // LEGACY GLASSMORPHISM (kept for compatibility)
    // ═══════════════════════════════════════════════════════════════
    val GlassBg = Color(0x33FFFFFF)               // 20% white
    val GlassBorder = Color(0x4DFFFFFF)           // 30% white
    val GlassShadow = Color(0x5E1F2683)           // rgba(31, 38, 135, 0.37)

    // Legacy hero gradients (kept for other screens)
    val HeroGradient1 = Color(0xFF1E1B4B)
    val HeroGradient2 = Color(0xFF312E81)
    val HeroGradient3 = Color(0xFF4C1D95)
    val HeroGradient4 = Color(0xFF1E3A5F)
    val HeroGradient5 = Color(0xFF0C4A6E)

    // ═══════════════════════════════════════════════════════════════
    // PROVIDER BRAND COLORS
    // ═══════════════════════════════════════════════════════════════
    val WiseGreen = Color(0xFF9FE870)
    val RemitlyBlue = Color(0xFF1A237E)
    val WesternUnionYellow = Color(0xFFFFDD00)
    val WorldRemitGreen = Color(0xFF00A651)
    val GCashBlue = Color(0xFF007DFE)
    val MayaGreen = Color(0xFF00D68F)
    val MoneyGramOrange = Color(0xFFFF6600)
    val XoomBlue = Color(0xFF003087)
}
