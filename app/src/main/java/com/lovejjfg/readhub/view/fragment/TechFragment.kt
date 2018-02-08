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
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.readhub.base.RefreshFragment
import com.lovejjfg.readhub.data.DataManager
import com.lovejjfg.readhub.data.topic.DataItem
import com.lovejjfg.readhub.utils.DateUtil
import com.lovejjfg.readhub.utils.JumpUitl
import com.lovejjfg.readhub.view.recycerview.NormalTopicAdapter
import io.reactivex.functions.Consumer


/**
 * ReadHub
 * Created by Joe at 2017/7/30.
 */
class TechFragment : RefreshFragment() {

    override fun createAdapter(): PowerAdapter<DataItem>? {
        return NormalTopicAdapter()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter?.setOnItemClickListener { _, _, item ->
            JumpUitl.jumpWeb(activity, item.url!!)
        }
    }

    override fun refresh(refresh: SwipeRefreshLayout?) {
        DataManager.subscribe(this, DataManager.init().tech(),
                Consumer {

                    if (it.data?.isNotEmpty()!!) {
                        order = DateUtil.parseTimeToMillis(it.data.last()?.publishDate)
                        adapter?.setList(it.data)
                        handleAlreadRead(it.data, {
                            TextUtils.equals(it?.id, latestOrder)
                        })
                        latestOrder = it.data.first()?.id
                    }
                    refresh?.isRefreshing = false
                },
                Consumer {
                    Log.i(TAG, "error:", it)
                    adapter?.showError()
                    handleError(it)
                    refresh?.isRefreshing = false
                })
    }

    override fun loadMore() {
        DataManager.subscribe(this, DataManager.init().techMore(order!!, 10),
                Consumer {
                    val data = it.data
                    order = DateUtil.parseTimeToMillis(data?.last()?.publishDate)
                    adapter?.appendList(data)
                    Log.i(TAG, "order:" + order)
                },
                Consumer {
                    Log.e(TAG, "error:", it)
                    adapter?.loadMoreError()

                })
    }

}