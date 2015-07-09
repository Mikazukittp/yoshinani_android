package app.android.ttp.mikazuki.yoshinani.domain.entity;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public class Payment {

    private String _id;
    private int amount;
    private String event;
    private String descrption;
    private String date;
    private User paidUser;
    private String paidUserId;
    private User[] participants;
    private String[] participantsIds;
    private boolean isDelete;
    private int _v;

    public Payment() {
    }

    public Payment(String _id, int amount, String event, String descrption, String date, User paidUser, String paidUserId, User[] participants, String[] participantsIds, boolean isDelete, int _v) {
        this._id = _id;
        this.amount = amount;
        this.event = event;
        this.descrption = descrption;
        this.date = date;
        this.paidUser = paidUser;
        this.paidUserId = paidUserId;
        this.participants = participants;
        this.participantsIds = participantsIds;
        this.isDelete = isDelete;
        this._v = _v;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getPaidUser() {
        return paidUser;
    }

    public void setPaidUser(User paidUser) {
        this.paidUser = paidUser;
    }

    public String getPaidUserId() {
        return paidUserId;
    }

    public void setPaidUserId(String paidUserId) {
        this.paidUserId = paidUserId;
    }

    public User[] getParticipants() {
        return participants;
    }

    public void setParticipants(User[] participants) {
        this.participants = participants;
    }

    public String[] getParticipantsIds() {
        return participantsIds;
    }

    public void setParticipantsIds(String[] participantsIds) {
        this.participantsIds = participantsIds;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public int get_v() {
        return _v;
    }

    public void set_v(int _v) {
        this._v = _v;
    }
}
