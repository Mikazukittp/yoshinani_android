package app.android.ttp.mikazuki.yoshinani.domain.entity;

/**
 * Created by haijimakazuki on 15/08/16.
 */
public class Total {

    private double paid;
    private double toPay;
    private int groupId;
    private int userId;

    public Total() {
    }

    public Total(long paid, long toPay, int groupId, int userId) {
        this.paid = paid;
        this.toPay = toPay;
        this.groupId = groupId;
        this.userId = userId;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getToPay() {
        return toPay;
    }

    public void setToPay(double toPay) {
        this.toPay = toPay;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
