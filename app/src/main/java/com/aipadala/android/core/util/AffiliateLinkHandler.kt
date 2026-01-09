package com.aipadala.android.core.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
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
        private const val TAG = "AffiliateLinkHandler"

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

        // Whitelist of allowed domains for affiliate links
        private val allowedDomains = setOf(
            "wise.com",
            "www.wise.com",
            "remitly.com",
            "www.remitly.com",
            "westernunion.com",
            "www.westernunion.com",
            "worldremit.com",
            "www.worldremit.com",
            "moneygram.com",
            "www.moneygram.com",
            "xoom.com",
            "www.xoom.com",
            "instarem.com",
            "www.instarem.com",
            "ofx.com",
            "www.ofx.com",
            "gopangea.com",
            "www.gopangea.com",
            "taptapsend.com",
            "www.taptapsend.com"
        )
    }

    /**
     * Validates if the given URL is from an allowed provider domain.
     * Returns true if the URL is safe to open, false otherwise.
     */
    private fun isValidAffiliateUrl(url: String): Boolean {
        return try {
            val uri = Uri.parse(url)
            val host = uri.host?.lowercase() ?: return false
            val scheme = uri.scheme?.lowercase()

            // Only allow HTTPS URLs
            if (scheme != "https") {
                Log.w(TAG, "Rejected non-HTTPS URL: $url")
                return false
            }

            // Check if host is in allowlist
            val isAllowed = allowedDomains.any { domain ->
                host == domain || host.endsWith(".$domain")
            }

            if (!isAllowed) {
                Log.w(TAG, "Rejected URL with unknown domain: $host")
            }

            isAllowed
        } catch (e: Exception) {
            Log.e(TAG, "Failed to validate URL: $url", e)
            false
        }
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
        // Validate URL before opening
        if (!isValidAffiliateUrl(url)) {
            analyticsTracker.trackError("affiliate_link", "Blocked invalid URL: $url")
            Log.e(TAG, "Attempted to open invalid affiliate URL: $url")
            return
        }

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
            Log.e(TAG, "Failed to open Custom Tab for URL: $url", e)
            // Fallback to regular browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            try {
                context.startActivity(intent)
            } catch (e2: Exception) {
                Log.e(TAG, "Failed to open browser for URL: $url", e2)
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
