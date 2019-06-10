/*
 * Copyright (c) 2017.  Joe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lovejjfg.readhub.view

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import com.lovejjfg.powerrecyclerx.PowerHolder
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.base.BaseActivity
import com.lovejjfg.readhub.base.BaseAdapter
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.data.DataManager
import com.lovejjfg.readhub.data.topic.InstantView
import com.lovejjfg.readhub.data.topic.detail.DetailItems
import com.lovejjfg.readhub.utils.JsoupUtils
import com.lovejjfg.readhub.utils.UIUtil
import com.lovejjfg.readhub.utils.inflate
import com.lovejjfg.readhub.utils.ioToMain
import com.lovejjfg.readhub.view.recycerview.ParseItemDerection
import com.lovejjfg.readhub.view.recycerview.holder.ImageParseHolder
import com.lovejjfg.readhub.view.recycerview.holder.TextParseHolder
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_instant_detail.instantList
import kotlinx.android.synthetic.main.activity_instant_detail.toolbar
import kotlinx.android.synthetic.main.activity_topic_detail.refreshContainer
import org.jsoup.Jsoup

/**
 * Created by joe on 2017/9/28.
 * Email: lovejjfg@gmail.com
 */
class InstantActivity : BaseActivity() {
    private lateinit var instantAdapter: InstantAdapter

    override fun getLayoutRes(): Int {
        return R.layout.activity_instant_detail
    }

    override fun afterCreatedView(savedInstanceState: Bundle?) {
        super.afterCreatedView(savedInstanceState)
        val topicId = intent.getStringExtra(Constants.ID)
        if (TextUtils.isEmpty(topicId)) {
            finish()
            return
        }
        refreshContainer.setOnRefreshListener {
            getData(topicId)
        }
        refreshContainer.isRefreshing = true
        toolbar.setOnClickListener {
            if (UIUtil.doubleClick()) {
                instantList.smoothScrollToPosition(0)
            }
        }
        instantList.addItemDecoration(ParseItemDerection())
        instantList.layoutManager = LinearLayoutManager(this)
        instantAdapter = InstantAdapter().apply {
            instantList.adapter = this
            enableLoadMore(false)
        }
        getData(topicId)
    }

    private fun getData(topicId: String) {
        DataManager.topicInstant(topicId)
            .ioToMain()
            .subscribe({
                handleInstant(it)
            }, {
                instantAdapter.showError()
                refreshContainer.isRefreshing = false
                handleError(it)
            })
            .addTo(mDisposables)
    }

    private fun handleInstant(instantView: InstantView?) {
        refreshContainer.isRefreshing = false
        toolbar.title = instantView?.title
        if (instantView == null) {
            Log.e("InstantActivity", "extra is null")
            return
        }
        Observable.create<DetailItems> {
            val body = Jsoup.parse(instantView.content).body().children()
            JsoupUtils.parse(body, it, "", 0)
            it.onComplete()
        }
            .toList()
            .subscribe({
                instantAdapter.clearList()
                instantAdapter.setList(it)
            }, {
                handleError(it)

            }).addTo(mDisposables)
    }

    class InstantAdapter : BaseAdapter<DetailItems>() {
        override fun getItemViewTypes(position: Int): Int {
            return list[position].type
        }

        override fun onViewHolderBind(holder: PowerHolder<DetailItems>, position: Int) {
            holder.onBind(list[position])
        }

        override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): PowerHolder<DetailItems> {
            return when (viewType) {
                Constants.TYPE_PARSE_TEXT -> {
                    TextParseHolder(parent.inflate(R.layout.holder_text_parse))
                }
                else -> {

                    ImageParseHolder(parent.inflate(R.layout.holder_img_parse))
                }
            }
        }
    }
}
