package com.lovejjfg.readhub.view

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.data.Constants

class WebActivity : AppCompatActivity() {

    var mWeb: WebView? = null
    var loading: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        loading = ProgressDialog(this)
//        val title = intent.getStringExtra(Constants.TITLE)
//        setMyTitle(title)
        mWeb = findViewById(R.id.web) as WebView?

        val url = intent.getStringExtra(Constants.URL)
        if (TextUtils.isEmpty(url)) {
            finish()
            return
        }
        mWeb!!.isVerticalScrollBarEnabled = false
        mWeb!!.isHorizontalScrollBarEnabled = false
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //            WebView.setWebContentsDebuggingEnabled(true);
        //        }
        val webSettings = mWeb!!.settings
        webSettings!!.javaScriptEnabled = true
        //        webSettings.setUseWideViewPort(true);
        //        webSettings.setLoadWithOverviewMode(true);
        mWeb!!.isClickable = true
        webSettings.domStorageEnabled = true
        webSettings.loadsImagesAutomatically = true
        webSettings.builtInZoomControls = true
        webSettings.blockNetworkImage = false
        webSettings.displayZoomControls = false
        //        mWeb.setWebViewClient(new WebViewClient());
        mWeb!!.setWebChromeClient(object : WebChromeClient() {
            override fun onReceivedTitle(webView: WebView, s: String) {
                super.onReceivedTitle(webView, s)
                if (TextUtils.isEmpty(title)) {
//                    setMyTitle(s)
                }
            }
        })
        mWeb!!.setWebViewClient(object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                loading?.show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                loading?.dismiss()

            }

        })

        mWeb!!.loadUrl(url)
    }

    override fun onDestroy() {
        super.onDestroy()
        mWeb?.destroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun onTitleDoubleClick() {
        mWeb!!.scrollTo(0, 0)
    }
}
