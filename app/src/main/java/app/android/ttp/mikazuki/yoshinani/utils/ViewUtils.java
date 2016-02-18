package app.android.ttp.mikazuki.yoshinani.utils;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

/**
 * Created by haijimakazuki on 16/02/05.
 */
public class ViewUtils {

    public static void disableTextInputLayoutHint(Activity activity, @IdRes int layoutId) {
        disableTextInputLayoutHint(activity.getWindow().getDecorView(), layoutId);
    }

    public static void disableTextInputLayoutHint(View window, @IdRes int layoutId) {
        View view = window.findViewById(layoutId);
        if (view instanceof TextInputLayout) {
            disableTextInputLayoutHint((TextInputLayout) view);
        }
    }

    public static void disableTextInputLayoutHint(@NonNull final TextInputLayout textInputLayout) {
        final CharSequence hint = textInputLayout.getHint();
        textInputLayout.setHint(null);
        EditText editText = textInputLayout.getEditText();
        if (editText != null) {
            editText.setHint(hint);
        }
    }
}
