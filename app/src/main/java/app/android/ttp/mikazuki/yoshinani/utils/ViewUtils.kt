package app.android.ttp.mikazuki.yoshinani.utils

import android.app.Activity
import android.content.Context
import android.support.annotation.IdRes
import android.support.design.widget.TextInputLayout
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * @author haijimakazuki
 */
object ViewUtils {

    fun disableTextInputLayoutHint(activity: Activity, @IdRes layoutId: Int) {
        disableTextInputLayoutHint(activity.window.decorView, layoutId)
    }

    fun disableTextInputLayoutHint(window: View, @IdRes layoutId: Int) {
        val view = window.findViewById<View>(layoutId)
        if (view is TextInputLayout) {
            disableTextInputLayoutHint(view)
        }
    }

    fun disableTextInputLayoutHint(textInputLayout: TextInputLayout) {
        val hint = textInputLayout.hint
        textInputLayout.hint = null
        val editText = textInputLayout.editText
        if (editText != null) {
            editText.hint = hint
        }
    }

    fun hideKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(null, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}
