package app.android.ttp.mikazuki.yoshinani.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.parceler.Parcel;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.User;
import rx.Observable;

/**
 * @author haijimakazuki
 */
@Parcel
public class UserModel {

    int id;
    String account;
    String username;
    String email;
    String token;
    List<TotalModel> totals;
    List<GroupModel> groups;
    List<GroupModel> invitedGroups;

    public UserModel() {
    }

    public UserModel(int id, String account, String username, String email, String token, List<TotalModel> totals, List<GroupModel> groups, List<GroupModel> invitedGroups) {
        this.id = id;
        this.account = account;
        this.username = username;
        this.email = email;
        this.token = token;
        this.totals = totals;
        this.groups = groups;
        this.invitedGroups = invitedGroups;
    }

    public static UserModel from(User entity) {
        if (entity == null) return null;
        return new UserModel(
                entity.getId(),
                entity.getAccount(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getToken(),
                TotalModel.from(entity.getTotals()),
                GroupModel.from(entity.getActiveGroups()),
                GroupModel.from(entity.getInvitedGroups())
        );
    }

    public static List<UserModel> from(List<User> entities) {
        if (entities == null) return null;
        return Observable
                .from(entities)
                .map(u -> UserModel.from(u))
                .toList()
                .toBlocking()
                .single();
    }

    public User createEntity() {
        return new User(
                this.id,
                this.account,
                this.username,
                this.email,
                this.token
        );
    }

    public boolean isMe(Context context) {
        return getId() == Integer.parseInt(PreferenceUtil.getUid(context));
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

    public List<TotalModel> getTotals() {
        return totals;
    }

    public void setTotals(List<TotalModel> totals) {
        this.totals = totals;
    }

    public List<GroupModel> getGroups() {
        return groups;
    }

    public void setGroups(final List<GroupModel> groups) {
        this.groups = groups;
    }

    public List<GroupModel> getInvitedGroups() {
        return invitedGroups;
    }

    public void setInvitedGroups(final List<GroupModel> invitedGroups) {
        this.invitedGroups = invitedGroups;
    }

    public Drawable getIcon() {
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(
                        String.valueOf(getAccount().charAt(0)),
                        ColorGenerator.MATERIAL.getColor(getAccount())
                );
        return drawable;
    }
}
