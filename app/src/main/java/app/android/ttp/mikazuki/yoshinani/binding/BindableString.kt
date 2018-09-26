package app.android.ttp.mikazuki.yoshinani.binding

import android.databinding.BaseObservable
import org.parceler.Parcel

/**
 * @author haijimakazuki
 */
@Parcel
class BindableString : BaseObservable() {

    internal var mValue: String? = null

    val isNotEmpty: Boolean
        get() = !isEmpty

    val isEmpty: Boolean
        get() = mValue == null || mValue!!.isEmpty()

    fun get(): String {
        return mValue ?: ""
    }

    fun set(value: String?) {
        if (mValue != value) {
            mValue = value
            notifyChange()
        }
    }
}