package app.android.ttp.mikazuki.yoshinani.view.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import app.android.ttp.mikazuki.yoshinani.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author haijimakazuki
 */
public class WebViewActivity extends AppCompatActivity {

    public static final String WEBVIEW_TITLE = "title";
    public static final String WEBVIEW_URL = "url";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.webview)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        // Toolbarの設定
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        // データの表示
        mToolbar.setTitle(getIntent().getStringExtra(WEBVIEW_TITLE));
        mWebView.loadUrl(getIntent().getStringExtra(WEBVIEW_URL));
    }
}
