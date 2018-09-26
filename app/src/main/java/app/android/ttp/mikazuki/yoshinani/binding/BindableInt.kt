package app.android.ttp.mikazuki.yoshinani.binding

import android.databinding.BaseObservable

import org.parceler.Parcel

import java.util.Objects

/**
 * Created by haijimakazuki on 16/01/26.
 */
@Parcel
class BindableInt : BaseObservable() {

    internal var mValue: Int = 0

    fun get(): Int {
        return mValue
    }

    fun set(value: Int) {
        if (mValue != value) {
            mValue = value
            notifyChange()
        }
    }
}