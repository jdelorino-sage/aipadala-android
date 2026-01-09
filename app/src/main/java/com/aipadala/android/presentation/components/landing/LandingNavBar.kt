package com.aipadala.android.presentation.components.landing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aipadala.android.R
import com.aipadala.android.presentation.theme.AIPadalaColors

@Composable
fun LandingNavBar(
    currentLanguage: String,
    isMobileMenuOpen: Boolean,
    onToggleLanguage: () -> Unit,
    onToggleMobileMenu: () -> Unit,
    onNavigateToCompare: () -> Unit,
    onNavigateToHowItWorks: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onSignInClick: () -> Unit,
    onGetStartedClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = AIPadalaColors.Primary900,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
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
                        text = "✨",
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AIPadalaColors.White
                )
            }

            // Desktop Navigation (visible on larger screens)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Language Toggle
                LanguageToggle(
                    currentLanguage = currentLanguage,
                    onToggle = onToggleLanguage
                )

                // Navigation Links (hidden on very small screens in real implementation)
                TextButton(onClick = onNavigateToCompare) {
                    Text(
                        text = stringResource(R.string.compare),
                        color = AIPadalaColors.White.copy(alpha = 0.9f)
                    )
                }

                TextButton(onClick = onNavigateToHowItWorks) {
                    Text(
                        text = stringResource(R.string.landing_how_it_works),
                        color = AIPadalaColors.White.copy(alpha = 0.9f)
                    )
                }

                // Sign In Button
                OutlinedButton(
                    onClick = onSignInClick,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = AIPadalaColors.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = stringResource(R.string.landing_sign_in))
                }

                // Get Started Button
                Button(
                    onClick = onGetStartedClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AIPadalaColors.CTAGradientStart
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.get_started),
                        color = AIPadalaColors.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Mobile Menu Toggle (for responsive design)
                IconButton(onClick = onToggleMobileMenu) {
                    Icon(
                        imageVector = if (isMobileMenuOpen) Icons.Default.Close else Icons.Default.Menu,
                        contentDescription = if (isMobileMenuOpen) "Close menu" else "Open menu",
                        tint = AIPadalaColors.White
                    )
                }
            }
        }
    }

    // Mobile Menu Drawer
    AnimatedVisibility(
        visible = isMobileMenuOpen,
        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(300)
        ),
        exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(300)
        )
    ) {
        MobileMenuDrawer(
            currentLanguage = currentLanguage,
            onToggleLanguage = onToggleLanguage,
            onNavigateToCompare = {
                onToggleMobileMenu()
                onNavigateToCompare()
            },
            onNavigateToHowItWorks = {
                onToggleMobileMenu()
                onNavigateToHowItWorks()
            },
            onNavigateToAbout = {
                onToggleMobileMenu()
                onNavigateToAbout()
            },
            onSignInClick = {
                onToggleMobileMenu()
                onSignInClick()
            },
            onGetStartedClick = {
                onToggleMobileMenu()
                onGetStartedClick()
            },
            onClose = onToggleMobileMenu
        )
    }
}

@Composable
private fun LanguageToggle(
    currentLanguage: String,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onToggle),
        color = AIPadalaColors.White.copy(alpha = 0.1f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "EN",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (currentLanguage == "en") FontWeight.Bold else FontWeight.Normal,
                color = if (currentLanguage == "en") AIPadalaColors.White else AIPadalaColors.White.copy(alpha = 0.6f)
            )
            Text(
                text = "/",
                style = MaterialTheme.typography.labelMedium,
                color = AIPadalaColors.White.copy(alpha = 0.5f)
            )
            Text(
                text = "FIL",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (currentLanguage == "fil") FontWeight.Bold else FontWeight.Normal,
                color = if (currentLanguage == "fil") AIPadalaColors.White else AIPadalaColors.White.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun MobileMenuDrawer(
    currentLanguage: String,
    onToggleLanguage: () -> Unit,
    onNavigateToCompare: () -> Unit,
    onNavigateToHowItWorks: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onSignInClick: () -> Unit,
    onGetStartedClick: () -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AIPadalaColors.Primary900.copy(alpha = 0.98f))
            .clickable(onClick = onClose)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Language Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                LanguageToggle(
                    currentLanguage = currentLanguage,
                    onToggle = onToggleLanguage
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Menu Items
            MobileMenuItem(
                text = stringResource(R.string.compare),
                onClick = onNavigateToCompare
            )

            MobileMenuItem(
                text = stringResource(R.string.landing_how_it_works),
                onClick = onNavigateToHowItWorks
            )

            MobileMenuItem(
                text = stringResource(R.string.about),
                onClick = onNavigateToAbout
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sign In Button
            OutlinedButton(
                onClick = onSignInClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AIPadalaColors.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.landing_sign_in),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Get Started Button
            Button(
                onClick = onGetStartedClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AIPadalaColors.CTAGradientStart
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.get_started),
                    color = AIPadalaColors.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun MobileMenuItem(
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = AIPadalaColors.White,
            fontWeight = FontWeight.Medium
        )
    }
}
