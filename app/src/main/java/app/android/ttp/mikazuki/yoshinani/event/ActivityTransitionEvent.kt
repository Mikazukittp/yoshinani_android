package app.android.ttp.mikazuki.yoshinani.event

import android.app.Activity
import android.os.Bundle

/**
 * Created by haijimakazuki on 15/09/11.
 */
class ActivityTransitionEvent @JvmOverloads constructor(val destinationActivity: Class<out Activity>,
                                                        addToBackStack: Boolean = true) : BaseEvent() {
    var bundle: Bundle? = null
    private var addToBackStack = true

    init {
        this.addToBackStack = addToBackStack
    }

    fun doesAddToBackStack(): Boolean {
        return addToBackStack
    }

    fun setAddToBackStack(addToBackStack: Boolean) {
        this.addToBackStack = addToBackStack
    }
}

