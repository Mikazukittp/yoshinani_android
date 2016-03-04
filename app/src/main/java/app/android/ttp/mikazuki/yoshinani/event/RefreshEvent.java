package app.android.ttp.mikazuki.yoshinani.event;

/**
 * @author haijimakazuki
 */
public class RefreshEvent {

    private boolean isForcibly;

    public RefreshEvent() {
        this(false);
    }

    public RefreshEvent(final boolean isForcibly) {
        this.isForcibly = isForcibly;
    }

    public boolean isForcibly() {
        return isForcibly;
    }

    public void setForcibly(final boolean forcibly) {
        isForcibly = forcibly;
    }
}
