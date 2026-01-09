package com.aipadala.android.presentation.components.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aipadala.android.R
import com.aipadala.android.presentation.theme.AIPadalaColors

@Composable
fun FooterSection(
    onCompareClick: (String) -> Unit,
    onLearnClick: (String) -> Unit,
    onCompanyClick: (String) -> Unit,
    onSocialClick: (String) -> Unit,
    onPrivacyClick: () -> Unit,
    onTermsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AIPadalaColors.Gray900)
            .padding(vertical = 40.dp, horizontal = 20.dp)
    ) {
        // Footer Content - Responsive Grid
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Logo and Tagline
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
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
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AIPadalaColors.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.landing_footer_tagline),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AIPadalaColors.Gray400
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Social Links
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SocialButton(
                        emoji = "📘",
                        onClick = { onSocialClick("facebook") }
                    )
                    SocialButton(
                        emoji = "📸",
                        onClick = { onSocialClick("instagram") }
                    )
                    SocialButton(
                        emoji = "🐦",
                        onClick = { onSocialClick("twitter") }
                    )
                    SocialButton(
                        emoji = "📺",
                        onClick = { onSocialClick("youtube") }
                    )
                    SocialButton(
                        emoji = "🎵",
                        onClick = { onSocialClick("tiktok") }
                    )
                }
            }

            // Links Grid - 2 columns on mobile
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Compare Column
                FooterLinkColumn(
                    title = stringResource(R.string.compare),
                    links = listOf(
                        stringResource(R.string.landing_usd_to_php) to "USD",
                        stringResource(R.string.landing_aud_to_php) to "AUD",
                        stringResource(R.string.landing_sar_to_php) to "SAR",
                        stringResource(R.string.landing_aed_to_php) to "AED",
                        stringResource(R.string.landing_sgd_to_php) to "SGD",
                        stringResource(R.string.landing_gbp_to_php) to "GBP"
                    ),
                    onLinkClick = onCompareClick,
                    modifier = Modifier.weight(1f)
                )

                // Learn Column
                FooterLinkColumn(
                    title = stringResource(R.string.landing_learn),
                    links = listOf(
                        stringResource(R.string.landing_financial_literacy) to "financial_literacy",
                        stringResource(R.string.landing_scam_protection) to "scam_protection",
                        stringResource(R.string.landing_budgeting_tips) to "budgeting_tips",
                        stringResource(R.string.landing_retirement_guide) to "retirement_guide"
                    ),
                    onLinkClick = onLearnClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Company Column
                FooterLinkColumn(
                    title = stringResource(R.string.landing_company),
                    links = listOf(
                        stringResource(R.string.landing_about_us) to "about",
                        stringResource(R.string.landing_contact) to "contact",
                        stringResource(R.string.landing_careers) to "careers",
                        stringResource(R.string.landing_press) to "press",
                        stringResource(R.string.landing_partners) to "partners"
                    ),
                    onLinkClick = onCompanyClick,
                    modifier = Modifier.weight(1f)
                )

                // Payout Methods
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.landing_payout_methods),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = AIPadalaColors.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PayoutMethodBadge(text = "💙 GCash")
                        PayoutMethodBadge(text = "💚 Maya")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PayoutMethodBadge(text = "🏦 Bank")
                        PayoutMethodBadge(text = "💵 Cash")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Divider
        HorizontalDivider(color = AIPadalaColors.Gray700)

        Spacer(modifier = Modifier.height(20.dp))

        // Bottom Footer
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Legal Links
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextButton(onClick = onPrivacyClick) {
                    Text(
                        text = stringResource(R.string.privacy_policy),
                        style = MaterialTheme.typography.labelMedium,
                        color = AIPadalaColors.Gray400
                    )
                }
                Text(
                    text = "•",
                    color = AIPadalaColors.Gray600
                )
                TextButton(onClick = onTermsClick) {
                    Text(
                        text = stringResource(R.string.terms_of_service),
                        style = MaterialTheme.typography.labelMedium,
                        color = AIPadalaColors.Gray400
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Copyright
            Text(
                text = stringResource(R.string.landing_copyright),
                style = MaterialTheme.typography.labelSmall,
                color = AIPadalaColors.Gray500,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun FooterLinkColumn(
    title: String,
    links: List<Pair<String, String>>,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = AIPadalaColors.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        links.forEach { (label, route) ->
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = AIPadalaColors.Gray400,
                modifier = Modifier
                    .clickable { onLinkClick(route) }
                    .padding(vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun SocialButton(
    emoji: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        color = AIPadalaColors.Gray800,
        shape = CircleShape
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun PayoutMethodBadge(text: String) {
    Surface(
        color = AIPadalaColors.Gray800,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = AIPadalaColors.Gray300,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
