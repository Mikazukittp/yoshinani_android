package app.android.ttp.mikazuki.yoshinani.binding;

import android.databinding.BaseObservable;
import android.support.annotation.Nullable;

import org.parceler.Parcel;

import java.util.Objects;

/**
 * Created by haijimakazuki on 16/01/26.
 */
@Parcel
public class BindableInt extends BaseObservable {

    int mValue;

    public int get() {
        return mValue;
    }

    public void set(@Nullable final int value) {
        if (!Objects.equals(mValue, value)) {
            mValue = value;
            notifyChange();
        }
    }
}