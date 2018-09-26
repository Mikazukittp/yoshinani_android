package app.android.ttp.mikazuki.yoshinani.binding

import android.databinding.BaseObservable

import org.parceler.Parcel

import java.util.Objects

/**
 * Created by haijimakazuki on 16/01/26.
 */
@Parcel
class BindableBoolean : BaseObservable() {
    internal var mValue: Boolean = false

    fun get(): Boolean {
        return mValue
    }

    fun set(value: Boolean) {
        if (mValue != value) {
            mValue = value
            notifyChange()
        }
    }
}