package app.android.ttp.mikazuki.yoshinani.event;

import app.android.ttp.mikazuki.yoshinani.model.Status;

/**
 * @author haijimakazuki
 */
public class MaintenanceEvent {

    private String title;
    private String message;
    private boolean active;

    public MaintenanceEvent(final String title,
                            final String message,
                            final boolean active) {
        this.title = title;
        this.message = message;
        this.active = active;
    }

    public MaintenanceEvent(Status status) {
        this.title = status.getTitle();
        this.message = status.getMessage();
        this.active = status.isActive();
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean isActive() {
        return active;
    }
}
