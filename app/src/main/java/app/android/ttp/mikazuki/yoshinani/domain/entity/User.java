package app.android.ttp.mikazuki.yoshinani.domain.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public class User implements Parcelable {

    private int id;
    private String account;
    private String username;
    private String email;
    private String token;
    private List<Total> totals;

    public User() {
    }

    public User(int id, String account, String username, String email, String token) {
        this.id = id;
        this.account = account;
        this.username = username;
        this.email = email;
        this.token = token;
    }

    protected User(Parcel in) {
        id = in.readInt();
        account = in.readString();
        username = in.readString();
        email = in.readString();
        token = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(account);
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(token);
    }
}
