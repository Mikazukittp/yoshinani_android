package app.android.ttp.mikazuki.yoshinani.viewModel;

import android.databinding.BaseObservable;

import java.util.Calendar;
import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author haijimakazuki
 */
public class PostPaymentViewModel extends BaseObservable implements Subscription {

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    private int mAmount;
    private String mEvent;
    private String mDescription;
    private Calendar mDate;
    private List<Integer> mParticipants;
    private boolean isButtonEnabled;

    public int getAmount() {
        return mAmount;
    }

    public void setAmount(final int amount) {
        this.mAmount = amount;
    }

    public String getEvent() {
        return mEvent;
    }

    public void setEvent(final String event) {
        this.mEvent = event;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(final String description) {
        this.mDescription = description;
    }

    public Calendar getDate() {
        return mDate;
    }

    public void setDate(final Calendar date) {
        this.mDate = date;
    }

    public List<Integer> getParticipants() {
        return mParticipants;
    }

    public void setParticipants(final List<Integer> participants) {
        this.mParticipants = participants;
    }

    public boolean isButtonEnabled() {
        return isButtonEnabled;
    }

    public void setButtonEnabled(final boolean buttonEnabled) {
        isButtonEnabled = buttonEnabled;
    }

    @Override
    public void unsubscribe() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public boolean isUnsubscribed() {
        return false;
    }
}
