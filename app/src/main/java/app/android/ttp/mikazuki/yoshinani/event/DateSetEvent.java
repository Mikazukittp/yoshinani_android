package app.android.ttp.mikazuki.yoshinani.event;

import java.util.Calendar;

/**
 * Created by haijimakazuki on 16/01/19.
 */
public class DateSetEvent {

    private final Calendar mDate;

    public DateSetEvent(Calendar date) {
        this.mDate = date;
    }

    public Calendar getDate() {
        return mDate;
    }

}
