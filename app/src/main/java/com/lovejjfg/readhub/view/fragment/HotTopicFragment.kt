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

package com.lovejjfg.readhub.view.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.View
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.readhub.base.RefreshFragment
import com.lovejjfg.readhub.data.DataManager
import com.lovejjfg.readhub.data.topic.DataItem
import io.reactivex.functions.Consumer


/**
 * ReadHub
 * Created by Joe at 2017/7/30.
 */
class HotTopicFragment : RefreshFragment() {

    override fun createAdapter(): PowerAdapter<DataItem>? {
        return HotTopicAdapter()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter?.setOnItemClickListener { _, position, item ->
            item.isExband = !item.isExband!!
            adapter?.notifyItemChanged(position)
        }
    }

    override fun refresh(refresh: SwipeRefreshLayout?) {
        DataManager.subscribe(this, DataManager.init().hotTopic(),
                Consumer {
                    val data = it.data
                    order = data?.last()?.order
                    Log.e(TAG, "refresh:order:" + order)
                    adapter?.setList(data)
                    refresh?.isRefreshing = false
                },
                Consumer {
                    it.printStackTrace()
                    adapter?.showError()
                    handleError(it)
                    refresh?.isRefreshing = false
                })
    }

    override fun loadMore() {
        DataManager.subscribe(this, DataManager.init().hotTopicMore(order!!, 10),
                Consumer {
                    val data = it.data
                    order = data?.last()?.order
                    adapter?.appendList(data)
                    Log.e(TAG, "loadMore:order:" + order)
                },
                Consumer {
                    it.printStackTrace()
                    adapter?.loadMoreError()
                })
    }
}