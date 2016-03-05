package app.android.ttp.mikazuki.yoshinani.model;

import org.parceler.Parcel;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.GroupUser;
import rx.Observable;

/**
 * @author haijimakazuki
 */
@Parcel
public class GroupUserModel {

    int id;
    int groupId;
    int userId;
    String status;

    public GroupUserModel(final int id, final int groupId, final int userId, final String status) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.status = status;
    }

    public GroupUserModel() {
    }

    public static GroupUserModel from(GroupUser entity) {
        return new GroupUserModel(
                entity.getId(),
                entity.getGroupId(),
                entity.getUserId(),
                entity.getStatus()
        );
    }

    public static List<GroupUserModel> from(List<GroupUser> entities) {
        return Observable.from(entities).map(gu -> GroupUserModel.from(gu)).toList().toBlocking().single();
    }

    public GroupUser createEntity() {
        return new GroupUser(
                this.getId(),
                this.getGroupId(),
                this.getUserId(),
                this.getStatus()
        );
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
