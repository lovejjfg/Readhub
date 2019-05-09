/*
 *
 *   Copyright (c) 2017.  Joe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.lovejjfg.readhub.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lovejjfg.readhub.BuildConfig
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.base.BaseActivity
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.utils.UIUtil
import kotlinx.android.synthetic.main.activity_web.pb
import kotlinx.android.synthetic.main.activity_web.toolbar
import kotlinx.android.synthetic.main.activity_web.web

class WebActivity : BaseActivity() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_web
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun afterCreatedView(savedInstanceState: Bundle?) {
        toolbar.setOnClickListener {
            if (UIUtil.doubleClick()) {
                onTitleDoubleClick()
            }
        }

        val url = intent.getStringExtra(Constants.URL)
        if (TextUtils.isEmpty(url)) {
            finish()
            return
        }
        web.isVerticalScrollBarEnabled = false
        web.isHorizontalScrollBarEnabled = false
        WebView.setWebContentsDebuggingEnabled(BuildConfig.IS_DEBUG)
        val webSettings = web.settings
        webSettings!!.javaScriptEnabled = true
        //        webSettings.setUseWideViewPort(true);
        //        webSettings.setLoadWithOverviewMode(true);
        web.isClickable = true
        webSettings.domStorageEnabled = true
        webSettings.loadsImagesAutomatically = true
        webSettings.builtInZoomControls = true
        webSettings.blockNetworkImage = false
        webSettings.displayZoomControls = false
        //        web.setWebViewClient(new WebViewClient());
        web.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(webView: WebView, s: String) {
                super.onReceivedTitle(webView, s)
                toolbar.title = s
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                pb.progress = newProgress
                if (newProgress == 100)
                    pb.visibility = View.GONE
            }
        }
        web.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                pb.show()
                pb.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                pb.visibility = View.GONE
//                pb.dismiss()
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                pb.visibility = View.GONE
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                super.onReceivedSslError(view, handler, error)
                pb.visibility = View.GONE
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                pb.visibility = View.GONE
            }
        }

        web.loadUrl(url)
    }

    override fun onDestroy() {
        super.onDestroy()
        web.destroy()
    }

    override fun onBackPressed() {
        if (web.canGoBack()) {
            web.goBack()
            return
        }
        super.onBackPressed()
    }

    private fun onTitleDoubleClick() {
        web.scrollTo(0, 0)
    }
}
