package app.android.ttp.mikazuki.yoshinani.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author haijimakazuki
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Status {
    private boolean active;
    private String title;
    private String message;

    public Status() {
    }

    public boolean isActive() {
        return active;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
