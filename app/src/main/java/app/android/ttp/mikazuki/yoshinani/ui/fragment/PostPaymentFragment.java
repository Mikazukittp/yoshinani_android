package app.android.ttp.mikazuki.yoshinani.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class PostPaymentFragment extends PostFragment implements DatePickerFragment.DatePickerDialogListener {

    private final String TAG = "PostPaymentFragment";
    static final int DATE_DIALOG_ID = 999;
    private OnFragmentInteractionListener mListener;
    private UserRepository mUserRepository;
    private PaymentRepository mPaymentRepository;

    @Bind(R.id.amount)
    TextView mAmount;
    @Bind(R.id.event)
    TextView mEvent;
    @Bind(R.id.description)
    TextView mDescription;
    @Bind(R.id.date)
    TextView mDate;


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

        mDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.setTargetFragment(PostPaymentFragment.this, DATE_DIALOG_ID);
                    newFragment.show(getFragmentManager(), "datePicker");
                    return false;
                }
                return true;
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @OnClick(R.id.user_select)
    public void onUserSelect(View v){
        mUserRepository = new RetrofitUserRepository(getActivity().getApplicationContext());
        mUserRepository.getAll(new BaseCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                CharSequence[] items = new CharSequence[users.size()];
                for (int i = 0; i < users.size(); i++) {
                    items[i] = users.get(i).getName();
                    Log.i("PostPaymentFragment", users.get(i).getName());
                }
                DialogFragment dialogFragment = new ContactUsDialogFragment();
                Bundle args = new Bundle();
                args.putCharSequenceArray("items", items);
                dialogFragment.setArguments(args);
                dialogFragment.show(getFragmentManager(), "contact_us");
            }

            @Override
            public void onFailure() {
                Log.e("PostPaymentFragment", "Fail to fetch users");
            }
        });
    }

    @OnClick(R.id.post)
    public void onPostPayment(View v){
        SharedPreferences sp = getActivity().getSharedPreferences("LocalData", Context.MODE_PRIVATE);
        Payment payment = new Payment(null,
                Integer.parseInt(mAmount.getText()+""),
                mEvent.getText().toString(),
                mDescription.getText().toString(),
                mDate.getText().toString(),
                null,
                sp.getString("myId", ""),
                null,
                new ArrayList<String>(),
                false,
                0);
        mPaymentRepository = new RetrofitPaymentRepository(getActivity().getApplicationContext());
        mPaymentRepository.create(payment, new BaseCallback<Payment>() {
            @Override
            public void onSuccess(Payment payment) {
                Log.d(TAG, "SEND!!");
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "ERROR!");
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }


    public static class ContactUsDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            CharSequence[] items =getArguments().getCharSequenceArray("items");
            final Activity activity = getActivity();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            Toast.makeText(activity, "使い方が押された", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            Toast.makeText(activity, "よくある質問が押された", Toast.LENGTH_LONG).show();
                            break;
                        case 2:
                            Toast.makeText(activity, "メールが押された", Toast.LENGTH_LONG).show();
                            break;
                        case 3:
                            Toast.makeText(activity, "閉じる", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            break;
                    }
                }
            });
            return builder.create();
        }
    }
}
