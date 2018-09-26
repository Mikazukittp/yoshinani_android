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
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author haijimakazuki
 */
public class PostRepaymentViewModel extends BaseObservable implements Subscription {
    private final Context mContext;
    private final int mGroupId;
    private List<UserModel> mAllUserModels = new ArrayList<>();

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    private BindableString mAmount = new BindableString();
    private Calendar mDate;
    private int mParticipant = -1;
    private boolean isPostEnabled;

    public PostRepaymentViewModel(Context context, int groupId) {
        mContext = context;
        mGroupId = groupId;
        reset();
    }

    public void setAllUsers(List<UserModel> allUsers) {
        mAllUserModels = allUsers;
        notifyChange();
    }

    public BindableString getAmount() {
        return mAmount;
    }

    public void setAmount(final String amount) {
        if (amount == null || TextUtils.unwrapCurrency(amount) != null) {
            mAmount.set(amount);
        }
    }

    @Bindable
    public Calendar getDate() {
        return mDate;
    }

    public void setDate(final Calendar date) {
        this.mDate = date;
        notifyChange();
    }

    public int getParticipantsId() {
        return mParticipant;
    }

    @Bindable
    public String getParticipants() {
        if (mParticipant == -1) {
            return "返済相手選択";
        }
        return mAllUserModels.get(mParticipant).getDisplayName();
    }

    public void setParticipants(int participants) {
        this.mParticipant = participants;
        notifyChange();
    }

    @Bindable
    public boolean getIsParticipantsEnabled() {
        return !mAllUserModels.isEmpty();
    }

    public PaymentModel getModel() {
        PaymentModel model = new PaymentModel();
        model.setId(-1);
        model.setGroupId(mGroupId);
        int uid = Integer.parseInt(PreferenceUtil.getUid(mContext));
        UserModel me = new UserModel();
        me.setId(uid);
        model.setPaidUser(me);
        model.setIsRepayment(true);

        model.setAmount(Integer.parseInt(mAmount.get()));
        model.setEvent("");
        model.setDescription("");
        model.setDate(mDate);
        List<UserModel> participants = new ArrayList<>();
        participants.add(mAllUserModels.get(mParticipant));
        model.setParticipants(participants);
        return model;
    }

    public void reset() {
        mAmount.set(null);
        mDate = ModelUtils.getToday();
        mParticipant = -1;
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
