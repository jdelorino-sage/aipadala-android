package com.aipadala.android.core.util

import android.content.Context
import android.os.Bundle
// import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsTracker @Inject constructor(
    private val context: Context
) {
    // Uncomment when Firebase is configured
    // private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun trackScreenView(screenName: String, screenClass: String) {
        val params = Bundle().apply {
            putString("screen_name", screenName)
            putString("screen_class", screenClass)
        }
        // firebaseAnalytics.logEvent("screen_view", params)
    }

    fun trackComparisonSearch(
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ) {
        val params = Bundle().apply {
            putString("from_currency", fromCurrency)
            putString("to_currency", toCurrency)
            putDouble("amount", amount)
        }
        // firebaseAnalytics.logEvent("comparison_search", params)
    }

    fun trackAffiliateClick(
        provider: String,
        corridor: String,
        amount: Double,
        source: String
    ) {
        val params = Bundle().apply {
            putString("provider", provider)
            putString("corridor", corridor)
            putDouble("amount", amount)
            putString("source", source)
        }
        // firebaseAnalytics.logEvent("affiliate_click", params)
    }

    fun trackAlertCreated(
        fromCurrency: String,
        threshold: Double,
        alertType: String
    ) {
        val params = Bundle().apply {
            putString("from_currency", fromCurrency)
            putDouble("threshold", threshold)
            putString("alert_type", alertType)
        }
        // firebaseAnalytics.logEvent("alert_created", params)
    }

    fun trackFavoriteAdded(fromCurrency: String, toCurrency: String) {
        val params = Bundle().apply {
            putString("from_currency", fromCurrency)
            putString("to_currency", toCurrency)
        }
        // firebaseAnalytics.logEvent("favorite_added", params)
    }

    fun trackProviderViewed(provider: String) {
        val params = Bundle().apply {
            putString("provider", provider)
        }
        // firebaseAnalytics.logEvent("provider_viewed", params)
    }

    fun trackLanguageChanged(language: String) {
        val params = Bundle().apply {
            putString("language", language)
        }
        // firebaseAnalytics.logEvent("language_changed", params)
    }

    fun trackError(errorType: String, errorMessage: String) {
        val params = Bundle().apply {
            putString("error_type", errorType)
            putString("error_message", errorMessage)
        }
        // firebaseAnalytics.logEvent("app_error", params)
    }
}
