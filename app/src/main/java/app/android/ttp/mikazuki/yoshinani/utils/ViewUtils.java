package app.android.ttp.mikazuki.yoshinani.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author haijimakazuki
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

    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(null, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
