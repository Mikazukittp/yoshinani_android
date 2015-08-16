package app.android.ttp.mikazuki.yoshinani.domain.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public class User implements Parcelable {

    private String _id;
    private String provider;
    private String name;
    private String email;
    private int currentPaid;
    private int currentHaveToPay;
    private String role;
    private String __v;

    public User() {
    }

    public User(String _id, String provider, String name, String email, int currentPaid, int currentHaveToPay, String role, String __v) {
        this._id = _id;
        this.provider = provider;
        this.name = name;
        this.email = email;
        this.currentPaid = currentPaid;
        this.currentHaveToPay = currentHaveToPay;
        this.role = role;
        this.__v = __v;
    }

    protected User(Parcel in) {
        _id = in.readString();
        provider = in.readString();
        name = in.readString();
        email = in.readString();
        currentPaid = in.readInt();
        currentHaveToPay = in.readInt();
        role = in.readString();
        __v = in.readString();
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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCurrentPaid() {
        return currentPaid;
    }

    public void setCurrentPaid(int currentPaid) {
        this.currentPaid = currentPaid;
    }

    public int getCurrentHaveToPay() {
        return currentHaveToPay;
    }

    public void setCurrentHaveToPay(int currentHaveToPay) {
        this.currentHaveToPay = currentHaveToPay;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(provider);
        parcel.writeString(name);
        parcel.writeString(email);
//        parcel.writeInt(currentPaid);
//        parcel.writeInt(currentHaveToPay);
        parcel.writeString(role);
        parcel.writeString(__v);
    }
}
