package app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity;

/**
 * @author haijimakazuki
 */
public class GroupUser {
    int id;
    int groupId;
    int userId;
    String status;

    public GroupUser(final int id, final int groupId, final int userId, final String status) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(final int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(final int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }
}
