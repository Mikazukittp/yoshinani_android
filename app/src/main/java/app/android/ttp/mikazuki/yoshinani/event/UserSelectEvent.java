package app.android.ttp.mikazuki.yoshinani.event;

/**
 * Created by haijimakazuki on 16/01/19.
 */
public class UserSelectEvent {

    private final int mValue;

    public UserSelectEvent(int value) {
        this.mValue = value;
    }

    public int getValue() {
        return mValue;
    }

}
