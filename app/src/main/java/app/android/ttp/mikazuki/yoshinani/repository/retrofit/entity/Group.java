package app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity;

import java.util.List;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public class Group {

    private int id;
    private String name;
    private String description;
    private List<User> activeUsers;
    private List<User> invitedUsers;

    public Group() {
    }

    public Group(int id, String name, String description, List<User> activeUsers, List<User> invitedUsers) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.activeUsers = activeUsers;
        this.invitedUsers = invitedUsers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(final List<User> activeUsers) {
        this.activeUsers = activeUsers;
    }

    public List<User> getInvitedUsers() {
        return invitedUsers;
    }

    public void setInvitedUsers(final List<User> invitedUsers) {
        this.invitedUsers = invitedUsers;
    }
}
