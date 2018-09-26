package app.android.ttp.mikazuki.yoshinani.utils

import android.util.Log

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

/**
 * @author haijimakazuki
 */
object ModelUtils {

    private val TAG = ModelUtils::class.java!!.getSimpleName()

    private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val today: Calendar
        get() {
            val cal = GregorianCalendar.getInstance()
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            return cal
        }

    @JvmOverloads
    fun parseDate(pattern: String, format: DateFormat = DATE_FORMAT): Calendar {
        var date: Date? = null
        try {
            date = format.parse(pattern)
        } catch (e: ParseException) {
            Log.e(TAG, "Failed to parse date: $pattern", e)
            e.printStackTrace()
        }

        val calendar = GregorianCalendar.getInstance()
        calendar.time = date
        return calendar
    }

    @JvmOverloads
    fun formatDate(date: Calendar, format: DateFormat = DATE_FORMAT): String {
        return format.format(date.time)
    }

}
