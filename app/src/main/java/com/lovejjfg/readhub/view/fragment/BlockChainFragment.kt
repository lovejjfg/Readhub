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

package com.lovejjfg.readhub.view.fragment

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.lovejjfg.powerrecyclerx.PowerAdapter
import com.lovejjfg.readhub.base.RefreshFragment
import com.lovejjfg.readhub.data.DataManager
import com.lovejjfg.readhub.data.topic.DataItem
import com.lovejjfg.readhub.utils.JumpUitl
import com.lovejjfg.readhub.utils.ioToMain
import com.lovejjfg.readhub.utils.parseTimeToMillis
import com.lovejjfg.readhub.view.recycerview.NormalTopicAdapter
import io.reactivex.rxkotlin.addTo

/**
 * ReadHub
 * Created by Joe at 2017/7/30.
 */
class BlockChainFragment : RefreshFragment() {

    override fun createAdapter(): PowerAdapter<DataItem> {
        return NormalTopicAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setOnItemClickListener { _, _, item ->
            JumpUitl.jumpWeb(mContext, item.url)
        }
    }

    override fun refresh(refresh: SwipeRefreshLayout?) {
        DataManager.blockchain()
            .ioToMain()
            .subscribe({ develop ->
                if (develop.data.isNotEmpty()) {
                    preLatestOrder = latestOrder
                    latestOrder = develop.data.first().id
                    order = develop.data.last().publishDate?.parseTimeToMillis()
                    adapter.setList(develop.data)
                    handleAlreadyRead(false, develop.data, develop.fromCache) {
                        TextUtils.equals(it?.id, preLatestOrder)
                    }
                }
                refresh?.isRefreshing = false
            },
                {
                    Log.e(TAG, "error:", it)
                    adapter.showError(false)
                    handleError(it)
                    refresh?.isRefreshing = false
                })
            .addTo(mDisposables)
    }

    override fun loadMore() {
        val order = order
        if (order == null || order.isEmpty()) {
            adapter.loadMoreError()
            return
        }
        DataManager.blockchainMore(order)
            .ioToMain()
            .subscribe({ develop ->
                val data = develop.data
                this.order = data.last().publishDate?.parseTimeToMillis()
                adapter.appendList(data)
                handleAlreadyRead(true, adapter.list) {
                    TextUtils.equals(it?.id, preLatestOrder)
                }
            },
                {
                    Log.e(TAG, "error:", it)
                    adapter.loadMoreError()

                })
            .addTo(mDisposables)
    }
}
