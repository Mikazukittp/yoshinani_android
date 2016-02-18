package app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity;

import java.util.List;

/**
 * @author haijimakazuki
 */
public class User {

    private int id;
    private String account;
    private String username;
    private String email;
    private String token;
    private List<Total> totals;
    private List<Group> activeGroups;
    private List<Group> invitedGroups;

    public User() {
    }

    public User(int id, String account, String username, String email, String token) {
        this.id = id;
        this.account = account;
        this.username = username;
        this.email = email;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Total> getTotals() {
        return totals;
    }

    public void setTotals(List<Total> totals) {
        this.totals = totals;
    }

    public List<Group> getActiveGroups() {
        return activeGroups;
    }

    public void setActiveGroups(final List<Group> activeGroups) {
        this.activeGroups = activeGroups;
    }

    public List<Group> getInvitedGroups() {
        return invitedGroups;
    }

    public void setInvitedGroups(final List<Group> invitedGroups) {
        this.invitedGroups = invitedGroups;
    }
}
