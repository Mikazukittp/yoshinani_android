package app.android.ttp.mikazuki.yoshinani.view.activity

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.webkit.WebView

import app.android.ttp.mikazuki.yoshinani.R
import butterknife.BindView
import butterknife.ButterKnife

/**
 * @author haijimakazuki
 */
class WebViewActivity : AppCompatActivity() {

    @BindView(R.id.toolbar)
    internal var mToolbar: Toolbar? = null
    @BindView(R.id.webview)
    internal var mWebView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        ButterKnife.bind(this)

        // Toolbarの設定
        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        mToolbar!!.setNavigationOnClickListener { v -> onBackPressed() }

        // データの表示
        mToolbar!!.title = intent.getStringExtra(WEBVIEW_TITLE)
        mWebView!!.loadUrl(intent.getStringExtra(WEBVIEW_URL))
    }

    companion object {

        val WEBVIEW_TITLE = "title"
        val WEBVIEW_URL = "url"
    }
}
