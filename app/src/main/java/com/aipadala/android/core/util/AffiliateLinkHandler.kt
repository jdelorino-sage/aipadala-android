package com.aipadala.android.core.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.aipadala.android.R
import com.aipadala.android.data.model.RemittanceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AffiliateLinkHandler @Inject constructor(
    private val context: Context,
    private val analyticsTracker: AnalyticsTracker
) {
    companion object {
        private val affiliateIds = mapOf(
            "wise" to "aipadala",
            "remitly" to "aipadala",
            "western_union" to "aipadala",
            "worldremit" to "aipadala",
            "moneygram" to "aipadala",
            "xoom" to "aipadala",
            "instarem" to "aipadala",
            "ofx" to "aipadala",
            "pangea" to "aipadala",
            "taptap_send" to "aipadala"
        )
    }

    fun buildAffiliateUrl(
        provider: RemittanceProvider,
        campaign: String = "comparison",
        sourceCurrency: String? = null,
        amount: Double? = null
    ): String {
        val baseUrl = provider.affiliateBaseUrl
        val affiliateId = affiliateIds[provider.name.lowercase()] ?: ""

        return buildString {
            append(baseUrl)
            if (affiliateId.isNotEmpty()) {
                append(affiliateId)
            }
            append("?utm_source=${Constants.UTM_SOURCE}")
            append("&utm_medium=${Constants.UTM_MEDIUM}")
            append("&utm_campaign=$campaign")
            sourceCurrency?.let { append("&sourceCurrency=$it") }
            amount?.let { append("&sourceAmount=$it") }
        }
    }

    fun openAffiliateLink(
        provider: RemittanceProvider,
        url: String,
        corridor: String,
        amount: Double
    ) {
        // Track the click
        analyticsTracker.trackAffiliateClick(
            provider = provider.name,
            corridor = corridor,
            amount = amount,
            source = "android_app"
        )

        // Build Custom Tab
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(ContextCompat.getColor(context, R.color.primary_500))
                    .build()
            )
            .setStartAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .build()

        try {
            customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            customTabsIntent.launchUrl(context, Uri.parse(url))
        } catch (e: Exception) {
            // Fallback to regular browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                analyticsTracker.trackError("affiliate_link", "Failed to open URL: $url")
            }
        }
    }

    fun openProviderApp(provider: RemittanceProvider): Boolean {
        val packageName = getProviderPackageName(provider)

        return try {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun getProviderPackageName(provider: RemittanceProvider): String {
        return when (provider) {
            RemittanceProvider.WISE -> "com.transferwise.android"
            RemittanceProvider.REMITLY -> "com.remitly.androidapp"
            RemittanceProvider.WESTERN_UNION -> "com.westernunion.moneytransferr3app"
            RemittanceProvider.WORLDREMIT -> "com.worldremit.android"
            RemittanceProvider.MONEYGRAM -> "com.moneygram.global"
            RemittanceProvider.XOOM -> "com.xoom.android"
            RemittanceProvider.INSTAREM -> "com.instarem.app"
            RemittanceProvider.OFX -> "com.ofx.mobile"
            RemittanceProvider.PANGEA -> "com.pangea.android"
            RemittanceProvider.TAPTAP_SEND -> "com.taptapsend.android"
        }
    }
}
