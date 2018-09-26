package app.android.ttp.mikazuki.yoshinani.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import app.android.ttp.mikazuki.yoshinani.binding.BindableString;
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil;
import app.android.ttp.mikazuki.yoshinani.utils.ModelUtils;
import app.android.ttp.mikazuki.yoshinani.utils.TextUtils;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author haijimakazuki
 */
public class PostPaymentViewModel extends BaseObservable implements Subscription {

    private final Context mContext;
    private final int mGroupId;
    private List<UserModel> mAllUserModels = new ArrayList<>();

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    private BindableString mAmount = new BindableString();
    private BindableString mEvent = new BindableString();
    private BindableString mDescription = new BindableString();
    private Calendar mDate;
    private List<Integer> mParticipants = new ArrayList<>();
    private boolean isParticipantsEnabled;
    private boolean isPostEnabled;

    public PostPaymentViewModel(Context context, int groupId) {
        mContext = context;
        mGroupId = groupId;
        reset();
    }

    public void setAllUsers(List<UserModel> allUsers) {
        mAllUserModels = allUsers;
    }

    public BindableString getAmount() {
        return mAmount;
    }

    public void setAmount(final String amount) {
        if (amount == null || TextUtils.unwrapCurrency(amount) != null) {
            mAmount.set(amount);
        }
    }

    public BindableString getEvent() {
        return mEvent;
    }

    public void setEvent(String event) {
        this.mEvent.set(event);
    }

    public BindableString getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription.set(description);
    }

    @Bindable
    public Calendar getDate() {
        return mDate;
    }

    public void setDate(final Calendar date) {
        this.mDate = date;
        notifyChange();
    }

    public ArrayList getParticipantsIdArray() {
        return (ArrayList) mParticipants;
    }

    @Bindable
    public String getParticipants() {
        final int maxDisplaySize = 3;

        if (mParticipants.isEmpty()) {
            return "参加者選択";
        }

        String names = (String) Observable.from(mParticipants)
                .limit(maxDisplaySize)
                .map(id -> mAllUserModels.get(id).getDisplayName())
                .reduce(null, (ns, n) -> ns == null ? n : ns + "," + n)
                .toBlocking().firstOrDefault("");
        if (mParticipants.size() > maxDisplaySize) {
            names += " 他" + (mParticipants.size() - maxDisplaySize) + "名";
        }
        return names;
    }

    public void setParticipants(final List<Integer> participants) {
        this.mParticipants = participants;
        notifyChange();
    }

    public PaymentModel getModel() {
        PaymentModel model = new PaymentModel();
        model.setId(-1);
        model.setGroupId(mGroupId);
        int uid = Integer.parseInt(PreferenceUtil.getUid(mContext));
        UserModel me = new UserModel();
        me.setId(uid);
        model.setPaidUser(me);
        model.setIsRepayment(false);

        model.setAmount(Integer.parseInt(mAmount.get()));
        model.setEvent(mEvent.get());
        model.setDescription(mDescription.get());
        model.setDate(mDate);
        List<UserModel> participants = new ArrayList<>();
        for (int i = 0; i < mParticipants.size(); i++) {
            participants.add(mAllUserModels.get(mParticipants.get(i)));
        }
        model.setParticipants(participants);
        return model;
    }

    public void reset() {
        mAmount.set(null);
        mEvent.set(null);
        mDescription.set(null);
        mDate = ModelUtils.getToday();
        mParticipants = new ArrayList<>();
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
