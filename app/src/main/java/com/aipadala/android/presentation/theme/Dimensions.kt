package com.aipadala.android.presentation.theme

import androidx.compose.ui.unit.dp

/**
 * Apple-inspired dimension system
 * Emphasizes generous whitespace and refined proportions
 */
object Dimensions {
    // ═══════════════════════════════════════════════════════════════
    // SPACING (Apple uses 8pt grid system)
    // ═══════════════════════════════════════════════════════════════
    val SpacingXxs = 2.dp
    val SpacingXs = 4.dp
    val SpacingSm = 8.dp
    val SpacingMd = 12.dp
    val SpacingLg = 16.dp
    val SpacingXl = 20.dp      // Apple commonly uses 20pt
    val SpacingXxl = 24.dp
    val Spacing3xl = 32.dp
    val Spacing4xl = 40.dp
    val Spacing5xl = 48.dp
    val Spacing6xl = 64.dp

    // ═══════════════════════════════════════════════════════════════
    // PADDING (generous whitespace like Apple)
    // ═══════════════════════════════════════════════════════════════
    val PaddingScreen = 20.dp      // iOS standard edge inset
    val PaddingCard = 16.dp
    val PaddingCardLarge = 20.dp   // For hero sections
    val PaddingButton = 16.dp      // Comfortable button padding
    val PaddingSection = 24.dp     // Between major sections

    // ═══════════════════════════════════════════════════════════════
    // CORNER RADIUS (Apple-style rounded corners)
    // ═══════════════════════════════════════════════════════════════
    val RadiusXs = 6.dp           // Small elements
    val RadiusSm = 10.dp          // iOS default small
    val RadiusMd = 12.dp          // Buttons, small cards
    val RadiusLg = 16.dp          // Cards
    val RadiusXl = 20.dp          // Large cards (Apple style)
    val RadiusXxl = 24.dp         // Hero sections
    val RadiusFull = 9999.dp      // Pills

    // ═══════════════════════════════════════════════════════════════
    // ICON SIZES
    // ═══════════════════════════════════════════════════════════════
    val IconSizeXs = 14.dp
    val IconSizeSm = 17.dp        // iOS tab bar icon size
    val IconSizeMd = 22.dp        // iOS standard icon
    val IconSizeLg = 24.dp
    val IconSizeXl = 28.dp
    val IconSize2xl = 32.dp
    val IconSize3xl = 44.dp       // Large action icons
    val IconSize4xl = 56.dp       // Hero icons

    // ═══════════════════════════════════════════════════════════════
    // COMPONENT SIZES
    // ═══════════════════════════════════════════════════════════════
    val ButtonHeightSm = 36.dp
    val ButtonHeightMd = 44.dp    // iOS default button height
    val ButtonHeightLg = 50.dp    // Large CTA buttons
    val ButtonHeightXl = 56.dp    // Hero CTA

    val InputHeight = 44.dp       // iOS text field height
    val CardMinHeight = 72.dp

    val BottomNavHeight = 83.dp   // iOS tab bar with safe area
    val TopBarHeight = 56.dp      // Cleaner, less dominant

    // Quick Action Tile
    val QuickActionTileSize = 80.dp
    val QuickActionIconSize = 28.dp

    // ═══════════════════════════════════════════════════════════════
    // PROVIDER & FLAGS
    // ═══════════════════════════════════════════════════════════════
    val ProviderLogoSm = 24.dp
    val ProviderLogoMd = 32.dp
    val ProviderLogoLg = 48.dp

    val FlagSizeSm = 20.dp
    val FlagSizeMd = 28.dp
    val FlagSizeLg = 40.dp

    // ═══════════════════════════════════════════════════════════════
    // TOUCH & ACCESSIBILITY
    // ═══════════════════════════════════════════════════════════════
    val TouchTargetMin = 44.dp    // iOS minimum (Android is 48, but 44 is acceptable)

    // ═══════════════════════════════════════════════════════════════
    // ELEVATION (Apple uses subtle shadows, not elevation)
    // These are kept for compatibility but prefer shadow approach
    // ═══════════════════════════════════════════════════════════════
    val ElevationNone = 0.dp
    val ElevationSm = 1.dp
    val ElevationMd = 2.dp
    val ElevationLg = 4.dp

    // ═══════════════════════════════════════════════════════════════
    // HERO SECTION
    // ═══════════════════════════════════════════════════════════════
    val HeroMinHeight = 200.dp
    val HeroMaxHeight = 280.dp
}
