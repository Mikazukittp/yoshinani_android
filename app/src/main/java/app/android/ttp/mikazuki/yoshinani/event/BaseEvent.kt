package app.android.ttp.mikazuki.yoshinani.event

/**
 * @author haijimakazuki
 */
open class BaseEvent {

    private var mTag: String? = null

    fun getTag(): String? {
        return mTag
    }

    fun setTag(mTag: String?): BaseEvent {
        this.mTag = mTag
        return this
    }

}
