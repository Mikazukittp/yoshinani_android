package app.android.ttp.mikazuki.yoshinani.domain.entity;

/**
 * Created by haijimakazuki on 15/07/07.
 */
public class Choice {
    private String choice;
    private String url;
    private int votes;

    public Choice() {
    }

    public Choice(String choice, String url, int votes) {
        this.choice = choice;
        this.url = url;
        this.votes = votes;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
