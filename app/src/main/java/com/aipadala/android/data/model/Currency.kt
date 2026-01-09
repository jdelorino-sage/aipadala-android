package com.aipadala.android.data.model

data class Currency(
    val code: String,
    val name: String,
    val symbol: String,
    val flagEmoji: String,
    val countryCode: String,
    val region: CurrencyRegion,
    val isPopular: Boolean = false
)

enum class CurrencyRegion {
    MIDDLE_EAST,
    ASIA_PACIFIC,
    AMERICAS,
    EUROPE,
    OCEANIA
}

object SupportedCurrencies {

    val PHP = Currency("PHP", "Philippine Peso", "₱", "🇵🇭", "PH", CurrencyRegion.ASIA_PACIFIC)

    val SOURCE_CURRENCIES = listOf(
        // Middle East (High OFW population)
        Currency("AED", "UAE Dirham", "د.إ", "🇦🇪", "AE", CurrencyRegion.MIDDLE_EAST, isPopular = true),
        Currency("SAR", "Saudi Riyal", "﷼", "🇸🇦", "SA", CurrencyRegion.MIDDLE_EAST, isPopular = true),
        Currency("QAR", "Qatari Riyal", "﷼", "🇶🇦", "QA", CurrencyRegion.MIDDLE_EAST),
        Currency("KWD", "Kuwaiti Dinar", "د.ك", "🇰🇼", "KW", CurrencyRegion.MIDDLE_EAST),
        Currency("BHD", "Bahraini Dinar", ".د.ب", "🇧🇭", "BH", CurrencyRegion.MIDDLE_EAST),
        Currency("OMR", "Omani Rial", "﷼", "🇴🇲", "OM", CurrencyRegion.MIDDLE_EAST),

        // Asia-Pacific
        Currency("SGD", "Singapore Dollar", "S$", "🇸🇬", "SG", CurrencyRegion.ASIA_PACIFIC, isPopular = true),
        Currency("HKD", "Hong Kong Dollar", "HK$", "🇭🇰", "HK", CurrencyRegion.ASIA_PACIFIC, isPopular = true),
        Currency("JPY", "Japanese Yen", "¥", "🇯🇵", "JP", CurrencyRegion.ASIA_PACIFIC),
        Currency("KRW", "South Korean Won", "₩", "🇰🇷", "KR", CurrencyRegion.ASIA_PACIFIC),
        Currency("TWD", "Taiwan Dollar", "NT$", "🇹🇼", "TW", CurrencyRegion.ASIA_PACIFIC),
        Currency("MYR", "Malaysian Ringgit", "RM", "🇲🇾", "MY", CurrencyRegion.ASIA_PACIFIC),

        // Americas
        Currency("USD", "US Dollar", "$", "🇺🇸", "US", CurrencyRegion.AMERICAS, isPopular = true),
        Currency("CAD", "Canadian Dollar", "C$", "🇨🇦", "CA", CurrencyRegion.AMERICAS, isPopular = true),

        // Europe
        Currency("EUR", "Euro", "€", "🇪🇺", "EU", CurrencyRegion.EUROPE, isPopular = true),
        Currency("GBP", "British Pound", "£", "🇬🇧", "GB", CurrencyRegion.EUROPE, isPopular = true),
        Currency("CHF", "Swiss Franc", "CHF", "🇨🇭", "CH", CurrencyRegion.EUROPE),
        Currency("NOK", "Norwegian Krone", "kr", "🇳🇴", "NO", CurrencyRegion.EUROPE),
        Currency("SEK", "Swedish Krona", "kr", "🇸🇪", "SE", CurrencyRegion.EUROPE),
        Currency("DKK", "Danish Krone", "kr", "🇩🇰", "DK", CurrencyRegion.EUROPE),

        // Oceania
        Currency("AUD", "Australian Dollar", "A$", "🇦🇺", "AU", CurrencyRegion.OCEANIA, isPopular = true),
        Currency("NZD", "New Zealand Dollar", "NZ$", "🇳🇿", "NZ", CurrencyRegion.OCEANIA)
    )

    val POPULAR_CURRENCIES = SOURCE_CURRENCIES.filter { it.isPopular }

    fun getByCode(code: String): Currency? =
        SOURCE_CURRENCIES.find { it.code.equals(code, ignoreCase = true) }

    fun getCurrenciesByRegion(region: CurrencyRegion): List<Currency> =
        SOURCE_CURRENCIES.filter { it.region == region }
}
