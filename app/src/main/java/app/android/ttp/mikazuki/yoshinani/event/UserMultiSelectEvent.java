package app.android.ttp.mikazuki.yoshinani.event;

import java.util.ArrayList;

/**
 * Created by haijimakazuki on 16/01/19.
 */
public class UserMultiSelectEvent {

    private final ArrayList<Integer> mValue;

    public UserMultiSelectEvent(ArrayList<Integer> value) {
        this.mValue = value;
    }

    public ArrayList<Integer> getValue() {
        return mValue;
    }

}
