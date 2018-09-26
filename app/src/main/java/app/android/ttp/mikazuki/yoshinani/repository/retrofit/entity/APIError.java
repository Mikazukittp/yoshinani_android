package app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity;


import java.util.HashMap;
import java.util.List;

/**
 * @author haijimakazuki
 */
public class APIError {

    private String message;
    private HashMap<String, List<String>> errors;

    public APIError(String message, HashMap<String, List<String>> errors) {
        this.message = message;
        this.errors = errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public String getDetailedMessage() {
        String result = this.message;
        if (hasErrors()) {
            for (List<String> attrErrors : errors.values()) {
                for (String error : attrErrors) {
                    result += "\n" + error;
                }
            }
        }
        return result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HashMap<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(HashMap<String, List<String>> errors) {
        this.errors = errors;
    }
}
