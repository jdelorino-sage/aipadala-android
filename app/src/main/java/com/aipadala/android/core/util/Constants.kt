package com.aipadala.android.core.util

object Constants {
    // Cache durations
    const val CACHE_DURATION_HOURS = 24L
    const val RATE_SYNC_INTERVAL_HOURS = 1L

    // Pagination
    const val DEFAULT_PAGE_SIZE = 20

    // Limits
    const val MAX_FAVORITE_CORRIDORS = 5
    const val MAX_ACTIVE_ALERTS = 10

    // Default amounts
    const val DEFAULT_SEND_AMOUNT = 500.0
    const val MIN_SEND_AMOUNT = 1.0
    const val MAX_SEND_AMOUNT = 100000.0

    // Target currency (always PHP for OFW use case)
    const val TARGET_CURRENCY = "PHP"

    // UTM parameters for affiliate tracking
    const val UTM_SOURCE = "aipadala"
    const val UTM_MEDIUM = "android_app"

    // Date formats
    const val DATE_FORMAT_DISPLAY = "MMM dd, yyyy"
    const val DATE_FORMAT_TIME = "HH:mm"
    const val DATE_FORMAT_FULL = "MMM dd, yyyy HH:mm"

    // Animation durations
    const val ANIMATION_DURATION_SHORT = 150
    const val ANIMATION_DURATION_MEDIUM = 300
    const val ANIMATION_DURATION_LONG = 500

    // Deep link paths
    const val DEEP_LINK_COMPARE = "compare"
    const val DEEP_LINK_ALERTS = "alerts"
    const val DEEP_LINK_PROVIDER = "provider"
}
