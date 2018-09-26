package app.android.ttp.mikazuki.yoshinani.event

/**
 * @author haijimakazuki
 */
class FetchListDataEvent<T>(val listData: List<T>?) : BaseEvent() {

    fun isType(clazz: Class<*>): Boolean {
        return listData != null && !listData.isEmpty() && clazz.isInstance(listData[0])
    }

}
