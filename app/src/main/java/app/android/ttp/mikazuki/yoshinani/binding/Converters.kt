package app.android.ttp.mikazuki.yoshinani.binding

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.support.v4.util.Pair
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView


import java.util.Calendar

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.model.TextWatcherAdapter
import app.android.ttp.mikazuki.yoshinani.utils.ModelUtils
import app.android.ttp.mikazuki.yoshinani.utils.TextUtils

/**
 * @author haijimakazuki
 */
object Converters {
    @BindingConversion
    fun convertBindableToString(bindableString: BindableString): String {
        return bindableString.get()
    }

    @BindingConversion
    fun convertBindableToString(bindableInt: BindableInt): String {
        return bindableInt.get().toString()
    }

    @BindingConversion
    fun convertBindableToBoolean(bindableBoolean: BindableBoolean): Boolean {
        return bindableBoolean.get()
    }

    @BindingConversion
    fun convertDate(date: Calendar): String {
        return ModelUtils.formatDate(date)
    }

    @BindingAdapter("android:text")
    fun bindEditText(view: EditText, bindableString: BindableString) {
        val pair = view.getTag(R.id.bound_observable) as Pair<*, *>
        if (pair == null || pair.first !== bindableString) {
            if (pair != null) {
                view.removeTextChangedListener(pair.second)
            }
            val watcher = object : TextWatcherAdapter() {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    bindableString.set(s.toString())
                }
            }
            view.setTag(R.id.bound_observable, Pair<BindableString, TextWatcherAdapter>(bindableString, watcher))
            view.addTextChangedListener(watcher)
        }
        val newValue = bindableString.get()
        if (view.text.toString() != newValue) {
            view.setText(newValue)
        }
    }

    @BindingAdapter("android:text")
    fun bindEditText(view: EditText, bindableInt: BindableInt) {
        val pair = view.getTag(R.id.bound_observable) as Pair<*, *>
        if (pair == null || pair.first !== bindableInt) {
            if (pair != null) {
                view.removeTextChangedListener(pair.second)
            }
            val watcher = object : TextWatcherAdapter() {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (android.text.TextUtils.isDigitsOnly(s.toString())) {
                        bindableInt.set(Integer.parseInt(s.toString()))
                    }
                }
            }
            view.setTag(R.id.bound_observable, Pair<BindableInt, TextWatcherAdapter>(bindableInt, watcher))
            view.addTextChangedListener(watcher)
        }
        val newValue = bindableInt.get().toString()
        if (view.text.toString() != newValue) {
            view.setText(newValue)
        }
    }

    @BindingAdapter("android:text")
    fun bindRadioGroup(view: RadioGroup, bindableBoolean: BindableBoolean) {
        if (view.getTag(R.id.bound_observable) !== bindableBoolean) {
            view.setTag(R.id.bound_observable, bindableBoolean)
            view.setOnCheckedChangeListener { group, checkedId -> bindableBoolean.set(checkedId == group.getChildAt(1).id) }
        }
        val newValue = bindableBoolean.get()
        (view.getChildAt(if (newValue) 1 else 0) as RadioButton).isChecked = true
    }

    @BindingAdapter("android:text")
    fun bindOnClick(view: View, runnable: Runnable) {
        view.setOnClickListener { runnable.run() }
    }

    @BindingAdapter("android:currency")
    fun bindCurrency(view: TextView, amount: Double) {
        view.text = TextUtils.wrapCurrency(amount)
    }
}