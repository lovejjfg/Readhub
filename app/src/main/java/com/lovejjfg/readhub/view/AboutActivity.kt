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

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import com.lovejjfg.powerrecyclerx.PowerHolder
import com.lovejjfg.readhub.BuildConfig
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.base.BaseActivity
import com.lovejjfg.readhub.base.BaseAdapter
import com.lovejjfg.readhub.base.BaseViewHolder
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.data.Library
import com.lovejjfg.readhub.utils.FirebaseUtils
import com.lovejjfg.readhub.utils.JumpUitl
import com.lovejjfg.readhub.utils.SharedPrefsUtil
import com.lovejjfg.readhub.utils.http.ToastUtil
import com.lovejjfg.readhub.utils.inflate
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_about.includeList
import kotlinx.android.synthetic.main.activity_about.readhubLogo
import kotlinx.android.synthetic.main.activity_about.versionText
import kotlinx.android.synthetic.main.holder_about_info.view.libDes
import kotlinx.android.synthetic.main.holder_about_info.view.libName

/**
 * ReadHub
 * Created by Joe at 2017/9/5.
 */
class AboutActivity : BaseActivity() {
    private lateinit var aboutAdapter: BaseAdapter<Library>
    private var takeRestCount = 0
    private var maxRestCount = 4
    private var showMarket: Boolean = true

    override fun getLayoutRes(): Int {
        return R.layout.activity_about
    }

    override fun afterCreatedView(savedInstanceState: Bundle?) {
        super.afterCreatedView(savedInstanceState)
        versionText.text = String.format(getString(R.string.version_at), BuildConfig.VERSION_NAME)
        includeList.layoutManager = LinearLayoutManager(this)
        aboutAdapter = AboutAdapter().apply {
            includeList.adapter = this
            enableLoadMore(false)
        }
        initData()
        showMarket = SharedPrefsUtil.getValue(this, Constants.SHOW_MARKET, true)
        aboutAdapter.setOnItemClickListener { _, _, item ->
            JumpUitl.jumpWeb(this, item.jumpUrl)
        }
        readhubLogo.setOnClickListener {
            handleLogoClick(showMarket)
        }
    }

    private fun handleLogoClick(showMarket: Boolean) {
        readhubLogo.animate()
            .rotation(readhubLogo.rotation + 3600f)
            .setDuration(1500)
            .setInterpolator(FastOutSlowInInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    takeRestCount++
                    readhubLogo.isEnabled = true
                    FirebaseUtils.logEggAbout(this@AboutActivity)
                    if (takeRestCount >= maxRestCount) {
                        if (showMarket) {
                            shareAppShop(this@AboutActivity)
                        }
                        takeRestCount = 0
                        readhubLogo.rotation = 0f
                    }
                }

                override fun onAnimationStart(animation: Animator?) {
                    readhubLogo.isEnabled = false
                    if (showMarket) {
                        ToastUtil.showToast(this@AboutActivity, "${maxRestCount - takeRestCount - 1}")
                    }
                }
            })
            .start()
        if (!EggsHelper.showCenterScaleView(this)) {
            return
        }
    }

    private fun shareAppShop(activity: Activity) {
        SharedPrefsUtil.putValue(this, Constants.SHOW_MARKET, false)
        showMarket = false
        val uri = Uri.parse("market://details?id=$packageName")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (intent.resolveActivity(packageManager) != null) {
            activity.startActivity(intent)
            FirebaseUtils.logMarketEvent(this)
        } else {
            ToastUtil.showToast(this, "装逼失败，0.0")
        }
    }

    private fun initData() {
        Observable.create<Library> {
            it.onNext(
                Library(
                    "Readhub",
                    "Readhub Android Client",
                    "https://github.com/lovejjfg/Readhub"
                )
            )
            it.onNext(
                Library(
                    "Android Jetpack AndroidX",
                    "AndroidX is the open-source project that the Android team uses to develop, test, package, version and release libraries within Jetpack.",
                    "https://developer.android.com/jetpack/androidx"
                )
            )
            it.onNext(
                Library(
                    "OkHttp",
                    "An HTTP & HTTP/2 client for Android and Java applications.",
                    "http://square.github.io/okhttp/"
                )
            )
            it.onNext(
                Library(
                    "RxKotlin",
                    "RxJava bindings for Kotlin.",
                    "https://github.com/ReactiveX/RxKotlin"
                )
            )
            it.onNext(
                Library(
                    "PowerRecyclerView",
                    "Easy for RecyclerView to pull refresh and load more.",
                    "https://github.com/lovejjfg/PowerRecyclerView"
                )
            )
            it.onNext(
                Library(
                    "Retrofit",
                    "A type-safe HTTP client for Android and Java.",
                    "http://square.github.io/retrofit/"
                )
            )
            it.onNext(
                Library(
                    "RxAndroid",
                    "RxJava bindings for Android.",
                    "https://github.com/ReactiveX/RxAndroid"
                )
            )
            it.onNext(
                Library(
                    "RxJava",
                    "RxJava – Reactive Extensions for the JVM – a library for composing asynchronous and event-based programs using observable sequences for the Java VM.",
                    "https://github.com/ReactiveX/RxJava"
                )
            )
            it.onNext(
                Library(
                    "Gson",
                    "A Java serialization/deserialization library to convert Java Objects into JSON and back",
                    "https://github.com/google/gson"
                )
            )

            it.onNext(
                Library(
                    "Glide",
                    "An image loading and caching library for Android focused on smooth scrolling.",
                    "https://github.com/bumptech/glide"
                )
            )

            it.onNext(
                Library(
                    "JSoup",
                    "Java HTML Parser, with best of DOM, CSS, and jquery.",
                    "https://github.com/jhy/jsoup/"
                )
            )
            it.onNext(
                Library(
                    "RxPermissions",
                    "Android runtime permissions powered by RxJava",
                    "https://github.com/tbruyelle/RxPermissions"
                )
            )

            it.onNext(
                Library(
                    "Kotlin",
                    "The Kotlin Programming Language",
                    "https://github.com/JetBrains/kotlin"
                )
            )
            it.onNext(
                Library(
                    "SwipeBack",
                    "The gesture and animation for page to go back.",
                    "https://github.com/lovejjfg/SwipeBack"
                )
            )

            it.onComplete()
        }.toSortedList { t, t1 ->
            t.name.compareTo(t1.name)
        }
            .subscribe({ aboutAdapter.setList(it) }, { it.printStackTrace() })
            .addTo(mDisposables)
    }

    class AboutAdapter : BaseAdapter<Library>() {
        override fun onViewHolderBind(holder: PowerHolder<Library>, position: Int) {
            holder.onBind(list[position])
        }

        override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): PowerHolder<Library>? {
            return AboutHolder(parent.inflate(R.layout.holder_about_info))
        }
    }

    class AboutHolder(itemView: View) : BaseViewHolder<Library>(itemView) {
        override fun onBind(t: Library) {
            itemView.libDes.text = t.des
            itemView.libName.text = t.name
        }
    }
}
