package app.android.ttp.mikazuki.yoshinani.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author haijimakazuki
 */
public class ModelUtils {

    private static final String TAG = ModelUtils.class.getSimpleName();

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static Calendar getToday() {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        return cal;
    }

    public static Calendar parseDate(String pattern) {
        return parseDate(pattern, DATE_FORMAT);
    }

    public static Calendar parseDate(String pattern, DateFormat format) {
        Date date = null;
        try {
            date = format.parse(pattern);
        } catch (ParseException e) {
            Log.e(TAG, "Failed to parse date: " + pattern, e);
            e.printStackTrace();
        }
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String formatDate(Calendar date) {
        return formatDate(date, DATE_FORMAT);
    }

    public static String formatDate(Calendar date, DateFormat format) {
        return format.format(date.getTime());
    }

}
