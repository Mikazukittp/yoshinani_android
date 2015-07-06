package app.android.ttp.mikazuki.yoshinani.domain.entity;

/**
 * Created by haijimakazuki on 15/07/07.
 */
public class Question {
    private String question;
    private String published_at;
    private String url;
    private Choice[] choices;

    public Question() {
    }

    public Question(String question, String published_at, String url, Choice[] choices) {
        this.question = question;
        this.published_at = published_at;
        this.url = url;
        this.choices = choices;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Choice[] getChoices() {
        return choices;
    }

    public void setChoices(Choice[] choices) {
        this.choices = choices;
    }
}
