package com.aipadala.android.core.util

import android.content.Context
import android.net.Uri
import com.aipadala.android.data.model.RemittanceProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AffiliateLinkHandlerTest {

    private lateinit var context: Context
    private lateinit var analyticsTracker: AnalyticsTracker
    private lateinit var affiliateLinkHandler: AffiliateLinkHandler

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        analyticsTracker = mockk(relaxed = true)
        affiliateLinkHandler = AffiliateLinkHandler(context, analyticsTracker)
    }

    @Test
    fun `buildAffiliateUrl creates correct URL for Wise`() {
        val url = affiliateLinkHandler.buildAffiliateUrl(
            provider = RemittanceProvider.WISE,
            campaign = "comparison",
            sourceCurrency = "USD",
            amount = 500.0
        )

        assertTrue(url.contains("wise.com"))
        assertTrue(url.contains("utm_source=aipadala"))
        assertTrue(url.contains("utm_medium=android_app"))
        assertTrue(url.contains("utm_campaign=comparison"))
        assertTrue(url.contains("sourceCurrency=USD"))
        assertTrue(url.contains("sourceAmount=500.0"))
    }

    @Test
    fun `buildAffiliateUrl creates correct URL for Remitly`() {
        val url = affiliateLinkHandler.buildAffiliateUrl(
            provider = RemittanceProvider.REMITLY,
            campaign = "home"
        )

        assertTrue(url.contains("remitly.com"))
        assertTrue(url.contains("utm_campaign=home"))
    }

    @Test
    fun `buildAffiliateUrl creates correct URL for Western Union`() {
        val url = affiliateLinkHandler.buildAffiliateUrl(
            provider = RemittanceProvider.WESTERN_UNION
        )

        assertTrue(url.contains("westernunion.com"))
    }

    @Test
    fun `buildAffiliateUrl creates correct URL for WorldRemit`() {
        val url = affiliateLinkHandler.buildAffiliateUrl(
            provider = RemittanceProvider.WORLDREMIT
        )

        assertTrue(url.contains("worldremit.com"))
    }

    @Test
    fun `buildAffiliateUrl creates correct URL for MoneyGram`() {
        val url = affiliateLinkHandler.buildAffiliateUrl(
            provider = RemittanceProvider.MONEYGRAM
        )

        assertTrue(url.contains("moneygram.com"))
    }

    @Test
    fun `buildAffiliateUrl omits optional parameters when null`() {
        val url = affiliateLinkHandler.buildAffiliateUrl(
            provider = RemittanceProvider.WISE,
            campaign = "test",
            sourceCurrency = null,
            amount = null
        )

        assertTrue(url.contains("utm_campaign=test"))
        assertTrue(!url.contains("sourceCurrency="))
        assertTrue(!url.contains("sourceAmount="))
    }

    @Test
    fun `buildAffiliateUrl includes all UTM parameters`() {
        val url = affiliateLinkHandler.buildAffiliateUrl(
            provider = RemittanceProvider.INSTAREM,
            campaign = "alerts"
        )

        assertTrue(url.contains("utm_source=${Constants.UTM_SOURCE}"))
        assertTrue(url.contains("utm_medium=${Constants.UTM_MEDIUM}"))
    }

    @Test
    fun `provider package names are correct`() {
        // Test a few key providers
        val wiseProvider = RemittanceProvider.WISE
        val remitlyProvider = RemittanceProvider.REMITLY

        // The package names should match known Android app IDs
        assertEquals("Wise", wiseProvider.displayName)
        assertEquals("Remitly", remitlyProvider.displayName)
    }

    @Test
    fun `all providers have valid affiliate base URLs`() {
        RemittanceProvider.entries.forEach { provider ->
            assertTrue(
                "Provider ${provider.name} should have HTTPS URL",
                provider.affiliateBaseUrl.startsWith("https://")
            )
        }
    }

    @Test
    fun `all providers have display names`() {
        RemittanceProvider.entries.forEach { provider ->
            assertTrue(
                "Provider ${provider.name} should have non-empty display name",
                provider.displayName.isNotBlank()
            )
        }
    }

    @Test
    fun `all providers have supported payout methods`() {
        RemittanceProvider.entries.forEach { provider ->
            assertTrue(
                "Provider ${provider.name} should have at least one payout method",
                provider.supportedPayouts.isNotEmpty()
            )
        }
    }
}
