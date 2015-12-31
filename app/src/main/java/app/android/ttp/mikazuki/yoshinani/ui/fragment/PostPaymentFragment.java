package app.android.ttp.mikazuki.yoshinani.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository.RetrofitPaymentRepository;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository.RetrofitUserRepository;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Payment;
import app.android.ttp.mikazuki.yoshinani.domain.entity.User;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import app.android.ttp.mikazuki.yoshinani.domain.repository.PaymentRepository;
import app.android.ttp.mikazuki.yoshinani.domain.repository.UserRepository;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class PostPaymentFragment extends PostFragment implements DatePickerFragment.DatePickerDialogListener {

    private final String TAG = "PostPaymentFragment";
    private static final int USER_SELECT = 111;
    private static final int DATE_DIALOG_ID = 999;
    private UserRepository mUserRepository;
    private PaymentRepository mPaymentRepository;
    private List<User> mUsers;
    private ArrayList<Integer> mSelected;

    @Bind(R.id.amount)
    TextView mAmount;
    @Bind(R.id.event)
    TextView mEvent;
    @Bind(R.id.description)
    TextView mDescription;
    @Bind(R.id.date)
    TextView mDate;
    @Bind(R.id.participants)
    TextView mParticipants;


    public static PostPaymentFragment newInstance() {
        PostPaymentFragment fragment = new PostPaymentFragment();
        return fragment;
    }

    public PostPaymentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_payment, container, false);
        ButterKnife.bind(this, view);

        mUserRepository = new RetrofitUserRepository(getActivity().getApplicationContext());
        mUserRepository.getAll(1, new BaseCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                mUsers = users;
            }

            @Override
            public void onFailure() {
                Log.e("PostPaymentFragment", "Fail to fetch mUsers");
            }
        });

        return view;
    }

    @OnTouch(R.id.date)
    public boolean onDateSelect(View v, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.setTargetFragment(PostPaymentFragment.this, DATE_DIALOG_ID);
            newFragment.show(getFragmentManager(), "datePicker");
            return false;
        }
        return true;
    }

    @OnClick(R.id.participants)
    public void onUserSelect(View v) {
        DialogFragment dialogFragment = new UserDialogFragment();
        Bundle args = new Bundle();
        CharSequence[] users = new CharSequence[mUsers.size()];
        for (int i = 0; i < users.length; i++) {
            users[i] = mUsers.get(i).getUsername();
        }
        args.putCharSequenceArray("users", users);
        args.putIntegerArrayList("selected", mSelected);
        dialogFragment.setArguments(args);
        dialogFragment.setTargetFragment(PostPaymentFragment.this, USER_SELECT);
        dialogFragment.show(getFragmentManager(), "contact_us");
    }

    // モーダルで選択されたユーザ名をViewに表示
    public void setParticipants(ArrayList<Integer> selected) {
        mSelected = selected;
        List<String> selectedUserName = new ArrayList<String>();
        for (int i = 0; i < Math.min(mSelected.size(), 3); i++) {
            selectedUserName.add(mUsers.get(mSelected.get(i)).getUsername());
            Log.d(TAG, mUsers.get(mSelected.get(i)).getUsername());
        }
        String names = TextUtils.join(", ", selectedUserName);
        if (mSelected.size() > 3) {
            names += " 他" + (mSelected.size() - 3) + "名";
        }
        mParticipants.setText(names);
    }


    @OnClick(R.id.post)
    public void onPostPayment(View v) {
        SharedPreferences sp = getActivity().getSharedPreferences("LocalData", Context.MODE_PRIVATE);
        User me = null;
        for (User user : mUsers) {
            if (user.getId() == sp.getInt("uid", -1)) {
                me = user;
                break;
            }
        }
        List<User> participants = new ArrayList<User>();
        for (int i = 0; i < mSelected.size(); i++) {
            participants.add(mUsers.get(mSelected.get(i)));
        }
        Payment payment = new Payment(-1,
                Integer.parseInt(mAmount.getText() + ""),
                mEvent.getText().toString(),
                mDescription.getText().toString(),
                mDate.getText().toString(),
                me,
                participants,
                true);
        mPaymentRepository = new RetrofitPaymentRepository(getActivity().getApplicationContext());
        mPaymentRepository.create(payment, new BaseCallback<Payment>() {
            @Override
            public void onSuccess(Payment payment) {
                mAmount.setText("");
                mEvent.setText("");
                mDescription.setText("");
                mDate.setText("");
                Log.d(TAG, "SEND!!");
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "ERROR!");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setDate(int year, int month, int day) {
        mDate.setText(year + " " + (month + 1) + "/" + day);
    }

}
