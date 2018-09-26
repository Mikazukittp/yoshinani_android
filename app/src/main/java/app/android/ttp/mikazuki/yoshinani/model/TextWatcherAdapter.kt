package app.android.ttp.mikazuki.yoshinani.model

import android.text.Editable
import android.text.TextWatcher

/**
 * @author haijimakazuki
 */
open class TextWatcherAdapter : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {}
}
