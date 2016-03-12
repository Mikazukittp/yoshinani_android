package app.android.ttp.mikazuki.yoshinani.event;

/**
 * @author haijimakazuki
 */
public class ErrorEvent {

    private String title;
    private String message;

    public ErrorEvent(final String title, final String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
