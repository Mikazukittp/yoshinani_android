package app.android.ttp.mikazuki.yoshinani.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository.RetrofitQuestionRepository;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Question;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import app.android.ttp.mikazuki.yoshinani.domain.repository.QuestionRepository;
import app.android.ttp.mikazuki.yoshinani.ui.adapter.QuestionListAdapter;
import app.android.ttp.mikazuki.yoshinani.ui.listener.ToolBarListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    @Bind(R.id.tool_bar)
    Toolbar mToolbar;
    @Bind(R.id.listview)
    ListView mListView;

    private QuestionRepository mQuestionRepository;
    private OnFragmentInteractionListener mListener;
    private ToolBarListener mToolbarListener;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToolbarListener.onMenuClicked();
            }
        });
        mQuestionRepository = new RetrofitQuestionRepository(getActivity().getApplicationContext());

        setListData();

        return view;
    }

    private void setListData() {
        mQuestionRepository.getAll(new BaseCallback<List<Question>>() {
            @Override
            public void onSuccess(List<Question> questions) {
                QuestionListAdapter adapter = new QuestionListAdapter(getActivity().getApplicationContext(), 0, questions);
                mListView.setAdapter(adapter);
            }

            @Override
            public void onFailure() {
                Log.e("!!!!!", "failure");
            }
        });
    }

    @OnClick(R.id.button)
    public void onButtonPressed(View v) {
        mListener.goToSubView();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            mToolbarListener = (ToolBarListener) activity;
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

    public interface OnFragmentInteractionListener {
        public void goToSubView();
    }

}
