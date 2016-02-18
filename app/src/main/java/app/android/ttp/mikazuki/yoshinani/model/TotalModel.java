package app.android.ttp.mikazuki.yoshinani.model;

import org.parceler.Parcel;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.Total;
import rx.Observable;

/**
 * @author haijimakazuki
 */
@Parcel
public class TotalModel {

    double paid;
    double toPay;
    int groupId;
    int userId;

    public TotalModel() {
    }

    public TotalModel(double paid, double toPay, int groupId, int userId) {
        this.paid = paid;
        this.toPay = toPay;
        this.groupId = groupId;
        this.userId = userId;
    }

    public static TotalModel from(Total entity) {
        return new TotalModel(
                entity.getPaid(),
                entity.getToPay(),
                entity.getGroupId(),
                entity.getUserId()
        );
    }

    public static List<TotalModel> from(List<Total> entities) {
        return Observable
                .from(entities)
                .map(t -> TotalModel.from(t))
                .toList()
                .toBlocking()
                .single();
    }

    public int getResult() {
        return (int) Math.round(getPaid() - getToPay());
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
