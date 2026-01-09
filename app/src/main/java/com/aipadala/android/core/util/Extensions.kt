package com.aipadala.android.core.util

import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Number formatting extensions
 */
fun Double.formatWithCommas(): String {
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    formatter.minimumFractionDigits = 2
    formatter.maximumFractionDigits = 2
    return formatter.format(this)
}

fun Double.formatCurrency(currencyCode: String): String {
    val symbol = getCurrencySymbol(currencyCode)
    return "$symbol${formatWithCommas()}"
}

fun Double.formatRate(): String {
    val formatter = DecimalFormat("#,##0.0000")
    return formatter.format(this)
}

fun Double.formatPercent(): String {
    val formatter = DecimalFormat("0.00")
    return "${formatter.format(this)}%"
}

private fun getCurrencySymbol(code: String): String {
    return when (code.uppercase()) {
        "USD" -> "$"
        "EUR" -> "€"
        "GBP" -> "£"
        "PHP" -> "₱"
        "AED" -> "د.إ"
        "SAR" -> "﷼"
        "SGD" -> "S$"
        "HKD" -> "HK$"
        "AUD" -> "A$"
        "CAD" -> "C$"
        "JPY" -> "¥"
        "KRW" -> "₩"
        "MYR" -> "RM"
        "QAR" -> "﷼"
        "KWD" -> "د.ك"
        "BHD" -> ".د.ب"
        "OMR" -> "﷼"
        "CHF" -> "CHF"
        "NOK", "SEK", "DKK" -> "kr"
        "NZD" -> "NZ$"
        "TWD" -> "NT$"
        else -> code
    }
}

/**
 * Date formatting extensions
 */
fun Long.toFormattedDate(pattern: String = Constants.DATE_FORMAT_DISPLAY): String {
    return try {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.format(Date(this))
    } catch (e: Exception) {
        ""
    }
}

fun Long.toRelativeTime(): String {
    val now = System.currentTimeMillis()
    val diff = now - this

    return when {
        diff < 60_000 -> "Just now"
        diff < 3_600_000 -> "${diff / 60_000} min ago"
        diff < 86_400_000 -> "${diff / 3_600_000} hours ago"
        diff < 604_800_000 -> "${diff / 86_400_000} days ago"
        else -> toFormattedDate()
    }
}

/**
 * String extensions
 */
fun String.capitalizeWords(): String {
    return split(" ").joinToString(" ") { word ->
        word.lowercase().replaceFirstChar { it.titlecase(Locale.getDefault()) }
    }
}

fun String?.orEmpty(default: String = ""): String = this ?: default

/**
 * Collection extensions
 */
fun <T> List<T>.safeSubList(fromIndex: Int, toIndex: Int): List<T> {
    return if (fromIndex >= size) {
        emptyList()
    } else {
        subList(fromIndex, minOf(toIndex, size))
    }
}
