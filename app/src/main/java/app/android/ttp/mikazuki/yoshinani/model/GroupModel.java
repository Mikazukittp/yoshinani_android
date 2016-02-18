package app.android.ttp.mikazuki.yoshinani.model;

import com.google.common.collect.Lists;

import org.parceler.Parcel;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.binding.BindableString;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.Group;
import rx.Observable;

/**
 * @author haijimakazuki
 */
@Parcel
public class GroupModel {

    int id;
    BindableString name = new BindableString();
    BindableString description = new BindableString();
    List<UserModel> members = Lists.newArrayList();
    List<UserModel> invitedMembers = Lists.newArrayList();

    public GroupModel() {
    }

    public GroupModel(int id, String name, String description, List<UserModel> members, List<UserModel> invitedMembers) {
        this.id = id;
        this.name.set(name);
        this.description.set(description);
        this.members = members;
        this.invitedMembers = invitedMembers;
    }

    public static GroupModel from(Group entity) {
        if (entity == null) return null;
        return new GroupModel(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                UserModel.from(entity.getActiveUsers()),
                UserModel.from(entity.getInvitedUsers())
        );
    }

    public static List<GroupModel> from(List<Group> entities) {
        if (entities == null) return null;
        return Observable.from(entities).map(GroupModel::from).toList().toBlocking().single();
    }

    public Group createEntity() {
        return new Group(
                this.getId(),
                this.getName().get(),
                this.getDescription().get(),
                Observable.from(this.getMembers()).map(UserModel::createEntity).toList().toBlocking().single(),
                Observable.from(this.getInvitedMembers()).map(UserModel::createEntity).toList().toBlocking().single()
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BindableString getName() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public BindableString getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public List<UserModel> getMembers() {
        return members;
    }

    public void setMembers(List<UserModel> members) {
        this.members = members;
    }

    public List<UserModel> getInvitedMembers() {
        return invitedMembers;
    }

    public void setInvitedMembers(final List<UserModel> invitedMembers) {
        this.invitedMembers = invitedMembers;
    }
}
