package app.android.ttp.mikazuki.yoshinani.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.android.ttp.mikazuki.yoshinani.BuildConfig;
import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.view.activity.WebViewActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author haijimakazuki
 */
public class AboutFragment extends Fragment {

    private static final String LICENSE_URL = "file:///android_asset/license.html";
    private static final String TERMS_URL = "file:///android_asset/terms.html";

    @BindView(R.id.version)
    TextView mVersion;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mVersion.setText(getString(R.string.version, BuildConfig.VERSION_NAME));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_about));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    @OnClick(R.id.terms)
    public void showPrivacyPolicy(View v) {
        startWebViewActivity("利用規約", TERMS_URL);
    }

    @OnClick(R.id.license)
    public void showLicense(View v) {
        startWebViewActivity("オープンソースライセンス", LICENSE_URL);
    }

    private void startWebViewActivity(@NonNull final String title,
                                      @NonNull final String url) {
        Bundle bundle = new Bundle();
        bundle.putString(WebViewActivity.WEBVIEW_TITLE, title);
        bundle.putString(WebViewActivity.WEBVIEW_URL, url);

        Intent i = new Intent(getActivity(), WebViewActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        i.putExtras(bundle);
        startActivity(i);
    }

}
