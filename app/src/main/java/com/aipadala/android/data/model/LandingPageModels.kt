package com.aipadala.android.data.model

/**
 * Data models for the Landing Page
 */

/**
 * Testimonial from OFW community
 */
data class Testimonial(
    val id: String,
    val name: String,
    val countryFlag: String,
    val countryName: String,
    val jobTitle: String,
    val quote: String,
    val monthlySavings: String,
    val avatarUrl: String? = null
)

/**
 * Stats displayed on the landing page
 */
data class LandingStats(
    val ofwFamilies: String = "10,000+",
    val totalSaved: String = "₱25M+",
    val averageRating: String = "4.9",
    val countries: String = "50+"
)

/**
 * Feature comparison between AI Padala and others
 */
data class FeatureComparison(
    val feature: String,
    val aiPadalaValue: String,
    val aiPadalaHasIt: Boolean,
    val othersValue: String,
    val othersHasIt: Boolean
)

/**
 * Step in the "How It Works" section
 */
data class HowItWorksStep(
    val stepNumber: Int,
    val titleKey: String,
    val descriptionKey: String,
    val icon: String
)

/**
 * Trust pillar in the "Why Trust Us" section
 */
data class TrustPillar(
    val icon: String,
    val titleKey: String,
    val descriptionKey: String
)

/**
 * Supported remittance corridor
 */
data class RemittanceCorridor(
    val fromCurrency: String,
    val toCurrency: String = "PHP",
    val fromCountry: String,
    val isPopular: Boolean = false
)

/**
 * Footer link item
 */
data class FooterLink(
    val titleKey: String,
    val route: String
)

/**
 * Footer section with title and links
 */
data class FooterSection(
    val titleKey: String,
    val links: List<FooterLink>
)

/**
 * Social media link
 */
data class SocialLink(
    val platform: String,
    val url: String,
    val icon: String
)

/**
 * Provider comparison result from quick compare
 */
data class QuickCompareResult(
    val providerId: String,
    val providerName: String,
    val providerLogoRes: Int? = null,
    val exchangeRate: Double,
    val transferFee: Double,
    val recipientGets: Double,
    val transferSpeed: String,
    val isBestRate: Boolean = false,
    val affiliateUrl: String? = null
)

/**
 * Payout method display data
 */
data class PayoutMethodDisplay(
    val name: String,
    val icon: String,
    val description: String
)

/**
 * Default testimonials data
 */
object DefaultTestimonials {
    val testimonials = listOf(
        Testimonial(
            id = "1",
            name = "Maria Santos",
            countryFlag = "🇦🇪",
            countryName = "UAE",
            jobTitle = "Nurse, Dubai",
            quote = "Dati, laging may kaltas sa padala ko. Ngayon sa AI Padala, buo ang padala ko sa pamilya. Mas madali at mas mabilis pa!",
            monthlySavings = "₱3,200/mo"
        ),
        Testimonial(
            id = "2",
            name = "Roberto Cruz",
            countryFlag = "🇸🇦",
            countryName = "Saudi Arabia",
            jobTitle = "Engineer, Riyadh",
            quote = "Ang galing ng AI rate finder! Sa isang click lang, nakita ko agad kung saan may best rate. Hindi na ako naghahanap pa sa ibang apps.",
            monthlySavings = "₱4,100/mo"
        ),
        Testimonial(
            id = "3",
            name = "Jennifer Reyes",
            countryFlag = "🇸🇬",
            countryName = "Singapore",
            jobTitle = "Domestic Helper",
            quote = "Simple lang gamitin. Within 5 minutes, naka-receive na ang nanay ko sa Pilipinas. Sobrang saya niya!",
            monthlySavings = "₱2,800/mo"
        ),
        Testimonial(
            id = "4",
            name = "Miguel Fernandez",
            countryFlag = "🇭🇰",
            countryName = "Hong Kong",
            jobTitle = "Construction Worker",
            quote = "Time saver talaga ang AI Padala. Hindi na ako pumipila sa remittance center. Mas malaki pa ang natatanggap ng asawa ko.",
            monthlySavings = "₱3,500/mo"
        ),
        Testimonial(
            id = "5",
            name = "Anna Garcia",
            countryFlag = "🇦🇺",
            countryName = "Australia",
            jobTitle = "Aged Care Worker",
            quote = "Hindi ko inexpect na ganun kabilis! First time ko magpadala gamit ang AI Padala, tapos na agad. Highly recommended!",
            monthlySavings = "₱5,200/mo"
        )
    )
}

/**
 * Default feature comparison data
 */
object DefaultFeatureComparisons {
    val comparisons = listOf(
        FeatureComparison(
            feature = "Transfer Fee",
            aiPadalaValue = "₱0 - ₱99",
            aiPadalaHasIt = true,
            othersValue = "₱150 - ₱500",
            othersHasIt = false
        ),
        FeatureComparison(
            feature = "Exchange Rate",
            aiPadalaValue = "AI-optimized, Real-time",
            aiPadalaHasIt = true,
            othersValue = "Fixed, Outdated",
            othersHasIt = false
        ),
        FeatureComparison(
            feature = "Transfer Speed",
            aiPadalaValue = "Instant - 24 hrs",
            aiPadalaHasIt = true,
            othersValue = "1-3 business days",
            othersHasIt = false
        ),
        FeatureComparison(
            feature = "Hidden Fees",
            aiPadalaValue = "Wala (None)",
            aiPadalaHasIt = true,
            othersValue = "Meron (Has)",
            othersHasIt = false
        ),
        FeatureComparison(
            feature = "24/7 Tagalog Support",
            aiPadalaValue = "✓",
            aiPadalaHasIt = true,
            othersValue = "✗",
            othersHasIt = false
        ),
        FeatureComparison(
            feature = "Smart Rate Alerts",
            aiPadalaValue = "✓",
            aiPadalaHasIt = true,
            othersValue = "✗",
            othersHasIt = false
        ),
        FeatureComparison(
            feature = "Multi-Provider Comparison",
            aiPadalaValue = "✓",
            aiPadalaHasIt = true,
            othersValue = "✗",
            othersHasIt = false
        ),
        FeatureComparison(
            feature = "Financial Literacy Resources",
            aiPadalaValue = "✓",
            aiPadalaHasIt = true,
            othersValue = "✗",
            othersHasIt = false
        )
    )
}

/**
 * Default How It Works steps
 */
object DefaultHowItWorksSteps {
    val steps = listOf(
        HowItWorksStep(
            stepNumber = 1,
            titleKey = "how_it_works_step1_title",
            descriptionKey = "how_it_works_step1_desc",
            icon = "person_add"
        ),
        HowItWorksStep(
            stepNumber = 2,
            titleKey = "how_it_works_step2_title",
            descriptionKey = "how_it_works_step2_desc",
            icon = "calculate"
        ),
        HowItWorksStep(
            stepNumber = 3,
            titleKey = "how_it_works_step3_title",
            descriptionKey = "how_it_works_step3_desc",
            icon = "send"
        )
    )
}

/**
 * Default Trust Pillars
 */
object DefaultTrustPillars {
    val pillars = listOf(
        TrustPillar(
            icon = "🔒",
            titleKey = "trust_security_title",
            descriptionKey = "trust_security_desc"
        ),
        TrustPillar(
            icon = "⚡",
            titleKey = "trust_speed_title",
            descriptionKey = "trust_speed_desc"
        ),
        TrustPillar(
            icon = "💰",
            titleKey = "trust_rates_title",
            descriptionKey = "trust_rates_desc"
        ),
        TrustPillar(
            icon = "💬",
            titleKey = "trust_support_title",
            descriptionKey = "trust_support_desc"
        )
    )
}

/**
 * Default Payout Methods
 */
object DefaultPayoutMethods {
    val methods = listOf(
        PayoutMethodDisplay(
            name = "GCash",
            icon = "💙",
            description = "Direct to GCash e-wallet"
        ),
        PayoutMethodDisplay(
            name = "Maya",
            icon = "💚",
            description = "Direct to Maya e-wallet"
        ),
        PayoutMethodDisplay(
            name = "Bank Transfer",
            icon = "🏦",
            description = "Any Philippine bank account"
        ),
        PayoutMethodDisplay(
            name = "Cash Pickup",
            icon = "💵",
            description = "Cebuana, LBC, Palawan, etc."
        )
    )
}
