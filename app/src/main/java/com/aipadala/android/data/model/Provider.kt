package com.aipadala.android.data.model

import com.aipadala.android.R

enum class RemittanceProvider(
    val displayName: String,
    val logoRes: Int,
    val brandColor: Long,
    val affiliateBaseUrl: String,
    val supportedPayouts: List<PayoutMethod>
) {
    WISE(
        displayName = "Wise",
        logoRes = R.drawable.logo_wise,
        brandColor = 0xFF9FE870,
        affiliateBaseUrl = "https://wise.com/invite/",
        supportedPayouts = listOf(PayoutMethod.BANK, PayoutMethod.GCASH, PayoutMethod.MAYA)
    ),
    REMITLY(
        displayName = "Remitly",
        logoRes = R.drawable.logo_remitly,
        brandColor = 0xFF1A237E,
        affiliateBaseUrl = "https://www.remitly.com/",
        supportedPayouts = listOf(
            PayoutMethod.BANK, PayoutMethod.GCASH, PayoutMethod.MAYA,
            PayoutMethod.CASH_PICKUP
        )
    ),
    WESTERN_UNION(
        displayName = "Western Union",
        logoRes = R.drawable.logo_western_union,
        brandColor = 0xFFFFDD00,
        affiliateBaseUrl = "https://www.westernunion.com/",
        supportedPayouts = listOf(
            PayoutMethod.BANK, PayoutMethod.GCASH, PayoutMethod.MAYA,
            PayoutMethod.CASH_PICKUP, PayoutMethod.DEBIT_CARD
        )
    ),
    WORLDREMIT(
        displayName = "WorldRemit",
        logoRes = R.drawable.logo_worldremit,
        brandColor = 0xFF00A651,
        affiliateBaseUrl = "https://www.worldremit.com/",
        supportedPayouts = listOf(
            PayoutMethod.BANK, PayoutMethod.GCASH, PayoutMethod.MAYA,
            PayoutMethod.CASH_PICKUP, PayoutMethod.MOBILE_LOAD
        )
    ),
    MONEYGRAM(
        displayName = "MoneyGram",
        logoRes = R.drawable.logo_moneygram,
        brandColor = 0xFFFF6600,
        affiliateBaseUrl = "https://www.moneygram.com/",
        supportedPayouts = listOf(
            PayoutMethod.BANK, PayoutMethod.CASH_PICKUP, PayoutMethod.MOBILE_WALLET
        )
    ),
    XOOM(
        displayName = "Xoom",
        logoRes = R.drawable.logo_xoom,
        brandColor = 0xFF003087,
        affiliateBaseUrl = "https://www.xoom.com/",
        supportedPayouts = listOf(
            PayoutMethod.BANK, PayoutMethod.GCASH, PayoutMethod.MAYA,
            PayoutMethod.CASH_PICKUP
        )
    ),
    INSTAREM(
        displayName = "Instarem",
        logoRes = R.drawable.logo_instarem,
        brandColor = 0xFF6366F1,
        affiliateBaseUrl = "https://www.instarem.com/",
        supportedPayouts = listOf(PayoutMethod.BANK, PayoutMethod.CASH_PICKUP)
    ),
    OFX(
        displayName = "OFX",
        logoRes = R.drawable.logo_ofx,
        brandColor = 0xFF1E3A8A,
        affiliateBaseUrl = "https://www.ofx.com/",
        supportedPayouts = listOf(PayoutMethod.BANK)
    ),
    PANGEA(
        displayName = "Pangea",
        logoRes = R.drawable.logo_pangea,
        brandColor = 0xFF10B981,
        affiliateBaseUrl = "https://www.gopangea.com/",
        supportedPayouts = listOf(PayoutMethod.BANK, PayoutMethod.CASH_PICKUP)
    ),
    TAPTAP_SEND(
        displayName = "Taptap Send",
        logoRes = R.drawable.logo_taptap,
        brandColor = 0xFF6366F1,
        affiliateBaseUrl = "https://www.taptapsend.com/",
        supportedPayouts = listOf(PayoutMethod.BANK, PayoutMethod.MOBILE_WALLET)
    );

    companion object {
        fun getByName(name: String): RemittanceProvider? =
            entries.find { it.name.equals(name, ignoreCase = true) }

        fun getByDisplayName(displayName: String): RemittanceProvider? =
            entries.find { it.displayName.equals(displayName, ignoreCase = true) }
    }
}

enum class PayoutMethod(val displayName: String, val iconRes: Int) {
    BANK("Bank Deposit", R.drawable.ic_bank),
    GCASH("GCash", R.drawable.ic_gcash),
    MAYA("Maya", R.drawable.ic_maya),
    CASH_PICKUP("Cash Pickup", R.drawable.ic_cash),
    DEBIT_CARD("Debit Card", R.drawable.ic_card),
    MOBILE_WALLET("Mobile Wallet", R.drawable.ic_wallet),
    MOBILE_LOAD("Mobile Load", R.drawable.ic_phone)
}
