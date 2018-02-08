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

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.powerrecycle.holder.PowerHolder
import com.lovejjfg.readhub.BuildConfig
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.data.Library
import com.lovejjfg.readhub.databinding.ActivityAboutBinding
import com.lovejjfg.readhub.databinding.HolderAboutInfoBinding
import com.lovejjfg.readhub.utils.JumpUitl
import io.reactivex.Observable


/**
 * ReadHub
 * Created by Joe at 2017/9/5.
 */
class AboutActivity : AppCompatActivity() {
    private var aboutAdapter: PowerAdapter<Library>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentView = DataBindingUtil.setContentView<ActivityAboutBinding>(this, R.layout.activity_about)
        contentView?.tvVersoin?.text = String.format("版本：%s", BuildConfig.VERSION_NAME)
        contentView?.toolbar?.setNavigationOnClickListener({ finish() })
        aboutAdapter = AboutAdapter()
        val recyclerView = contentView?.recyclerView
        recyclerView?.layoutManager = LinearLayoutManager(this)
        aboutAdapter?.attachRecyclerView(recyclerView!!)
        initData()
        aboutAdapter?.setOnItemClickListener({ _, _, item ->
            JumpUitl.jumpWeb(this, item.jumpUrl)
        })
    }

    private fun initData() {
        Observable.create<Library> {
            it.onNext(Library("Readhub",
                    "Readhub Android 客户端",
                    "https://github.com/lovejjfg/Readhub"))
            it.onNext(Library("Android support libraries",
                    "The Android support libraries offer a number of features that are not built into the framework.",
                    "https://developer.android.com/topic/libraries/support-library"))
            it.onNext(Library("OkHttp",
                    "An HTTP & HTTP/2 client for Android and Java applications.",
                    "http://square.github.io/okhttp/"))
            it.onNext(Library("RxKotlin",
                    "RxJava bindings for Kotlin.",
                    "https://github.com/ReactiveX/RxKotlin"))
            it.onNext(Library("PowerRecyclerView",
                    "Easy for RecyclerView to pull refresh and load more.",
                    "https://github.com/lovejjfg/PowerRecyclerView"))
            it.onNext(Library("Retrofit",
                    "A type-safe HTTP client for Android and Java.",
                    "http://square.github.io/retrofit/"))
            it.onNext(Library("RxAndroid",
                    "RxJava bindings for Android.",
                    "https://github.com/ReactiveX/RxAndroid"))
            it.onNext(Library("RxJava",
                    "RxJava – Reactive Extensions for the JVM – a library for composing asynchronous and event-based programs using observable sequences for the Java VM.",
                    "https://github.com/ReactiveX/RxJava"))
            it.onNext(Library("Gson",
                    "A Java serialization/deserialization library to convert Java Objects into JSON and back",
                    "https://github.com/google/gson"))

            it.onNext(Library("Glide",
                    "An image loading and caching library for Android focused on smooth scrolling.",
                    "https://github.com/bumptech/glide"))

            it.onNext(Library("JSoup",
                    "Java HTML Parser, with best of DOM, CSS, and jquery.",
                    "https://github.com/jhy/jsoup/"))
            it.onNext(Library("RxPermissions",
                    "Android runtime permissions powered by RxJava",
                    "https://github.com/tbruyelle/RxPermissions"))
            it.onComplete()
        }.toSortedList({ t, t1 ->
            t.name!!.compareTo(t1.name!!)
        })
                .subscribe({ aboutAdapter?.setList(it) }, { it.printStackTrace() })


    }


    class AboutAdapter : PowerAdapter<Library>() {
        override fun onViewHolderBind(holder: PowerHolder<Library>?, position: Int) {
            holder?.onBind(list[position])
        }

        override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): PowerHolder<Library>? {
            val infoBinding = DataBindingUtil.inflate<HolderAboutInfoBinding>(LayoutInflater.from(parent.context), R.layout.holder_about_info, parent, false)
            return AboutHolder(infoBinding)
        }

    }

    class AboutHolder(itemView: HolderAboutInfoBinding) : PowerHolder<Library>(itemView.root) {
        private var dataBind: HolderAboutInfoBinding? = itemView
        override fun onBind(t: Library?) {
            dataBind?.lib = t
        }
    }
}