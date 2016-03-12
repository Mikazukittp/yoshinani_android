package app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity;

import com.google.common.collect.Maps;

import java.util.HashMap;

/**
 * @author haijimakazuki
 */
public class APIError {

    private String message;
    private HashMap<String, String> errors = Maps.newHashMap();

    public APIError(String message, HashMap<String, String> errors) {
        this.message = message;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HashMap<String, String> getErrors() {
        return errors;
    }

    public void setErrors(HashMap<String, String> errors) {
        this.errors = errors;
    }
}
