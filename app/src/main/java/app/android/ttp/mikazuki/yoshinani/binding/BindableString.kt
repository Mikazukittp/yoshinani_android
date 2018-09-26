package app.android.ttp.mikazuki.yoshinani.binding

import android.databinding.BaseObservable

import org.parceler.Parcel

import java.util.Objects

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
        return if (mValue != null) mValue else ""
    }

    fun set(value: String?) {
        if (mValue != value) {
            mValue = value
            notifyChange()
        }
    }
}