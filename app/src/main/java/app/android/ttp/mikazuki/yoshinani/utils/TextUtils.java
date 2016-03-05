package app.android.ttp.mikazuki.yoshinani.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * @author haijimakazuki
 */
public class TextUtils {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getCurrencyInstance();
    private static final Currency CURRENCY = Currency.getInstance(Locale.getDefault());

    @NonNull
    public static String wrapCurrency(double amount) {
        return NUMBER_FORMAT.format(amount);
    }

    @Nullable
    public static Double unwrapCurrency(String currencyText) {
        String unwrapText = currencyText;
        unwrapText = unwrapText.replace(CURRENCY.getSymbol(), "");
        unwrapText = unwrapText.replace(",", "");
        try {
            return Double.parseDouble(unwrapText);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
