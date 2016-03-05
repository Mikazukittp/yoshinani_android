package app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity;

import java.util.List;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public class Payment {

    private int id;
    private int amount;
    private String event;
    private String description;
    private String date;
    private User paidUser;
    private List<User> participants;
    private int groupId;
    private boolean isRepayment;

    public Payment() {
    }

    public Payment(int id, int amount, String event, String description, String date, User paidUser, List<User> participants, int groupId, boolean isRepayment) {
        this.id = id;
        this.amount = amount;
        this.event = event;
        this.description = description;
        this.date = date;
        this.paidUser = paidUser;
        this.participants = participants;
        this.groupId = groupId;
        this.isRepayment = isRepayment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(final int groupId) {
        this.groupId = groupId;
    }

    public boolean isRepayment() {
        return isRepayment;
    }

    public void setIsRepayment(boolean isRepayment) {
        this.isRepayment = isRepayment;
    }
}
