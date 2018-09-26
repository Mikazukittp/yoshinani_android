package app.android.ttp.mikazuki.yoshinani.utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * @author haijimakazuki
 */
object TextUtils {

    private val NUMBER_FORMAT = NumberFormat.getCurrencyInstance()
    private val CURRENCY = Currency.getInstance(Locale.getDefault())

    fun wrapCurrency(amount: Double): String {
        return NUMBER_FORMAT.format(amount)
    }

    fun unwrapCurrency(currencyText: String): Double? {
        var unwrapText = currencyText
        unwrapText = unwrapText.replace(CURRENCY.symbol, "")
        unwrapText = unwrapText.replace(",", "")
        try {
            return java.lang.Double.parseDouble(unwrapText)
        } catch (e: NumberFormatException) {
            return null
        }

    }
}
