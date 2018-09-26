package app.android.ttp.mikazuki.yoshinani.event

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by haijimakazuki on 16/01/17.
 */
class FragmentTransitionEvent(val destinationFragment: Fragment) {
    var bundle: Bundle? = null
    private var addToBackStack = true

    fun doesAddToBackStack(): Boolean {
        return addToBackStack
    }

    fun setAddToBackStack(addToBackStack: Boolean) {
        this.addToBackStack = addToBackStack
    }
}
