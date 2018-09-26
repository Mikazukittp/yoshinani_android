package app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity


import java.util.HashMap

/**
 * @author haijimakazuki
 */
class APIError(var message: String?, var errors: HashMap<String, List<String>>?) {

    val detailedMessage: String
        get() {
            var result = this.message
            if (hasErrors()) {
                for (attrErrors in errors!!.values) {
                    for (error in attrErrors) {
                        result += "\n" + error
                    }
                }
            }
            return result
        }

    fun hasErrors(): Boolean {
        return !errors!!.isEmpty()
    }
}
