package app.android.ttp.mikazuki.yoshinani.model;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.parceler.Parcel;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import app.android.ttp.mikazuki.yoshinani.BR;
import app.android.ttp.mikazuki.yoshinani.binding.BindableBoolean;
import app.android.ttp.mikazuki.yoshinani.binding.BindableInt;
import app.android.ttp.mikazuki.yoshinani.binding.BindableString;
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.Payment;
import app.android.ttp.mikazuki.yoshinani.utils.ModelUtils;
import rx.Observable;

/**
 * @author haijimakazuki
 */
@Parcel
public class PaymentModel extends BaseObservable {

    int id;
    BindableInt amount = new BindableInt();
    BindableString event = new BindableString();
    BindableString description = new BindableString();
    Calendar date = ModelUtils.getToday();
    UserModel paidUser;
    List<UserModel> participants;
    int groupId;
    BindableBoolean isRepayment = new BindableBoolean();

    public PaymentModel() {
    }

    public PaymentModel(int id, int amount, String event, String description, Calendar date, UserModel paidUser, List<UserModel> participants, int groupId, boolean isRepayment) {
        this.id = id;
        this.amount.set(amount);
        this.event.set(event);
        this.description.set(description);
        this.date = date;
        this.paidUser = paidUser;
        this.participants = participants;
        this.groupId = groupId;
        this.isRepayment.set(isRepayment);
    }

    public static PaymentModel from(Payment entity) {

        return new PaymentModel(
                entity.getId(),
                entity.getAmount(),
                entity.getEvent(),
                entity.getDescription(),
                ModelUtils.parseDate(entity.getDate()),
                UserModel.from(entity.getPaidUser()),
                Observable.from(entity.getParticipants()).map(u -> UserModel.from(u)).toList().toBlocking().single(),
                entity.getGroupId(),
                entity.isRepayment()
        );
    }

    public static List<PaymentModel> from(List<Payment> entities) {
        return Observable
                .from(entities)
                .map(p -> PaymentModel.from(p))
                .toList()
                .toBlocking()
                .single();
    }

    public Payment createEntity() {
        return new Payment(
                this.id,
                this.amount.get(),
                this.event.get(),
                this.description.get(),
                ModelUtils.formatDate(this.date),
                this.paidUser.createEntity(),
                Observable.from(this.participants).map(p -> p.createEntity()).toList().toBlocking().single(),
                this.groupId,
                this.isRepayment.get()
        );
    }

    public void reset() {
        this.id = -1;
        this.amount.set(0);
        this.event.set("");
        this.description.set("");
//        this.date = new GregorianCalendar();
    }

    public boolean paid(Context context) {
        return Objects.equals(String.valueOf(getPaidUser().getId()), PreferenceUtil.getUid(context));
    }

    public boolean participate(Context context) {
        return Observable.from(getParticipants())
                .map(UserModel::getId)
                .map(String::valueOf)
                .contains(PreferenceUtil.getUid(context))
                .toBlocking().single();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BindableInt getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    public BindableString getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event.set(event);
    }

    public BindableString getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    @Bindable
    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    public UserModel getPaidUser() {
        return paidUser;
    }

    public void setPaidUser(UserModel paidUser) {
        this.paidUser = paidUser;
    }

    public List<UserModel> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserModel> participants) {
        this.participants = participants;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(final int groupId) {
        this.groupId = groupId;
    }

    public BindableBoolean isRepayment() {
        return isRepayment;
    }

    public void setIsRepayment(boolean isRepayment) {
        this.isRepayment.set(isRepayment);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PaymentModel that = (PaymentModel) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
