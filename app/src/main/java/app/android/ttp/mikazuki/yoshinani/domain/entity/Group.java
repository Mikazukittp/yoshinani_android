package app.android.ttp.mikazuki.yoshinani.domain.entity;

import java.util.List;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public class Group {

    private String _id;
    private String name;
    private List<User> members;
    private int _v;

    public Group() {
    }

    public Group(String _id, String name, List<User> members, int _v) {
        this._id = _id;
        this.name = name;
        this.members = members;
        this._v = _v;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public int get_v() {
        return _v;
    }

    public void set_v(int _v) {
        this._v = _v;
    }
}
