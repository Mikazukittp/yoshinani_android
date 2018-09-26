package app.android.ttp.mikazuki.yoshinani.event

/**
 * @author haijimakazuki
 */
class FetchDataEvent<T>(val data: T?) : BaseEvent() {

    fun isType(clazz: Class<*>): Boolean {
        return data != null && clazz.isInstance(data)
    }
}
