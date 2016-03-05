package app.android.ttp.mikazuki.yoshinani.binding;

import android.databinding.BaseObservable;
import android.support.annotation.Nullable;

import org.parceler.Parcel;

import java.util.Objects;

/**
 * @author haijimakazuki
 */
@Parcel
public class BindableString extends BaseObservable {

    String mValue;

    public String get() {
        return mValue != null ? mValue : "";
    }

    public void set(@Nullable final String value) {
        if (!Objects.equals(mValue, value)) {
            mValue = value;
            notifyChange();
        }
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        return mValue == null || mValue.isEmpty();
    }
}