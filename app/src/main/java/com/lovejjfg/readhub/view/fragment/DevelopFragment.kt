package com.lovejjfg.readhub.view.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.View
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.readhub.data.DataManager
import com.lovejjfg.readhub.data.topic.DataItem
import io.reactivex.functions.Consumer


/**
 * ReadHub
 * Created by Joe at 2017/7/30.
 */
class DevelopFragment : RefreshFragment() {

    override fun createAdapter(): PowerAdapter<DataItem>? {
        return NormalTopicAdapter()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter?.setOnItemClickListener { itemView, position, item ->

        }
    }

    override fun refresh(refresh: SwipeRefreshLayout?) {
        DataManager.subscribe(DataManager.init().devNews(),
                Consumer {
                    val data = it.data
                    order = data?.last()?.id
                    Log.e(TAG, "order:" + order)
                    adapter?.setList(data)
                    refresh?.isRefreshing = false
                },
                Consumer {
                    Log.e(TAG, "error:", it)
                    refresh?.isRefreshing = false
                })
    }

    override fun loadMore() {
        DataManager.subscribe(DataManager.init().devNewsMore(order!!, 10),
                Consumer {
                    val data = it.data
                    order = data?.last()?.order
                    adapter?.appendList(data)
                    Log.e(TAG, "order:" + order)
                },
                Consumer {
                    Log.e(TAG, "error:", it)
                    adapter?.loadMoreError()

                })
    }

}