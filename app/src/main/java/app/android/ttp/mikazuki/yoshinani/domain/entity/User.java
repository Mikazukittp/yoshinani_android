package app.android.ttp.mikazuki.yoshinani.domain.entity;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public class User {

    private String _id;
    private String provider;
    private String name;
    private String email;
    private int currentPaid;
    private int currentHaveToPay;
    private String role;
    private String _v;

    public User() {
    }

    public User(String _id, String provider, String name, String email, int paid, int haveToPay, String role, String _v) {
        this._id = _id;
        this.provider = provider;
        this.name = name;
        this.email = email;
        this.currentPaid = paid;
        this.currentHaveToPay = haveToPay;
        this.role = role;
        this._v = _v;
    }

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

    public String get_v() {
        return _v;
    }

    public void set_v(String _v) {
        this._v = _v;
    }
}
