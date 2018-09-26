package app.android.ttp.mikazuki.yoshinani.binding;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import java.util.Calendar;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.model.TextWatcherAdapter;
import app.android.ttp.mikazuki.yoshinani.utils.ModelUtils;
import app.android.ttp.mikazuki.yoshinani.utils.TextUtils;

/**
 * @author haijimakazuki
 */
public class Converters {
    @BindingConversion
    public static String convertBindableToString(BindableString bindableString) {
        return bindableString.get();
    }

    @NonNull
    @BindingConversion
    public static String convertBindableToString(BindableInt bindableInt) {
        return String.valueOf(bindableInt.get());
    }

    @BindingConversion
    public static boolean convertBindableToBoolean(BindableBoolean bindableBoolean) {
        return bindableBoolean.get();
    }

    @BindingConversion
    public static String convertDate(Calendar date) {
        return ModelUtils.formatDate(date);
    }

    @BindingAdapter({"android:text"})
    public static void bindEditText(EditText view, final BindableString bindableString) {
        Pair<BindableString, TextWatcherAdapter> pair = (Pair) view.getTag(R.id.bound_observable);
        if (pair == null || pair.first != bindableString) {
            if (pair != null) {
                view.removeTextChangedListener(pair.second);
            }
            TextWatcherAdapter watcher = new TextWatcherAdapter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bindableString.set(s.toString());
                }
            };
            view.setTag(R.id.bound_observable, new Pair<>(bindableString, watcher));
            view.addTextChangedListener(watcher);
        }
        String newValue = bindableString.get();
        if (!view.getText().toString().equals(newValue)) {
            view.setText(newValue);
        }
    }

    @BindingAdapter({"android:text"})
    public static void bindEditText(EditText view, final BindableInt bindableInt) {
        Pair<BindableInt, TextWatcherAdapter> pair = (Pair) view.getTag(R.id.bound_observable);
        if (pair == null || pair.first != bindableInt) {
            if (pair != null) {
                view.removeTextChangedListener(pair.second);
            }
            TextWatcherAdapter watcher = new TextWatcherAdapter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (android.text.TextUtils.isDigitsOnly(s.toString())) {
                        bindableInt.set(Integer.parseInt(s.toString()));
                    }
                }
            };
            view.setTag(R.id.bound_observable, new Pair<>(bindableInt, watcher));
            view.addTextChangedListener(watcher);
        }
        String newValue = String.valueOf(bindableInt.get());
        if (!view.getText().toString().equals(newValue)) {
            view.setText(newValue);
        }
    }

    @BindingAdapter({"android:text"})
    public static void bindRadioGroup(RadioGroup view, final BindableBoolean bindableBoolean) {
        if (view.getTag(R.id.bound_observable) != bindableBoolean) {
            view.setTag(R.id.bound_observable, bindableBoolean);
            view.setOnCheckedChangeListener((group, checkedId) -> bindableBoolean.set(checkedId == group.getChildAt(1).getId()));
        }
        Boolean newValue = bindableBoolean.get();
        ((RadioButton) view.getChildAt(newValue ? 1 : 0)).setChecked(true);
    }

    @BindingAdapter({"android:text"})
    public static void bindOnClick(View view, final Runnable runnable) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runnable.run();
            }
        });
    }

    @BindingAdapter("android:currency")
    public static void bindCurrency(TextView view, final double amount) {
        view.setText(TextUtils.wrapCurrency(amount));
    }
}