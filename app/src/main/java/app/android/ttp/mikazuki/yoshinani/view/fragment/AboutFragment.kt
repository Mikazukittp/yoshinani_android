package app.android.ttp.mikazuki.yoshinani.view.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import app.android.ttp.mikazuki.yoshinani.BuildConfig
import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.view.activity.WebViewActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder

/**
 * @author haijimakazuki
 */
class AboutFragment : Fragment() {

    @BindView(R.id.version)
    internal var mVersion: TextView? = null

    private var mUnbinder: Unbinder? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        mUnbinder = ButterKnife.bind(this, view)
        mVersion!!.text = getString(R.string.version, BuildConfig.VERSION_NAME)
        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.menu_about)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder!!.unbind()
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    @OnClick(R.id.terms)
    fun showPrivacyPolicy(v: View) {
        startWebViewActivity("利用規約", TERMS_URL)
    }

    @OnClick(R.id.license)
    fun showLicense(v: View) {
        startWebViewActivity("オープンソースライセンス", LICENSE_URL)
    }

    private fun startWebViewActivity(title: String,
                                     url: String) {
        val bundle = Bundle()
        bundle.putString(WebViewActivity.WEBVIEW_TITLE, title)
        bundle.putString(WebViewActivity.WEBVIEW_URL, url)

        val i = Intent(activity, WebViewActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        i.putExtras(bundle)
        startActivity(i)
    }

    companion object {

        private val LICENSE_URL = "file:///android_asset/license.html"
        private val TERMS_URL = "file:///android_asset/terms.html"
    }

}
