package com.lovejjfg.readhub.fragment

import android.app.Fragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lovejjfg.readhub.DataManager
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.data.Test
import com.lovejjfg.readhub.databinding.LayoutRefreshRecyclerBinding
import io.reactivex.functions.Consumer


/**
 * ReadHub
 * Created by Joe at 2017/7/30.
 */
class HotTopicFragment : Fragment() {
    val TAG = "HotTopicFragment"
    var order: Int? = 0
    var binding: LayoutRefreshRecyclerBinding? = null
    var adapter: HotTopicAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate<LayoutRefreshRecyclerBinding>(inflater, R.layout.layout_refresh_recycler, container, false)
        val root = binding?.root
        return root!!
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvHot = binding?.rvHot
        rvHot?.layoutManager = LinearLayoutManager(activity)
        adapter = HotTopicAdapter()
        rvHot?.adapter = adapter
        val refresh = binding?.container
        adapter?.setLoadMoreListener { loadMore() }
        adapter?.totalCount = Int.MAX_VALUE
        refresh?.isRefreshing = true
        refresh?.setOnRefreshListener { refresh(refresh) }
        adapter?.setOnItemClickListener { _, position, item ->
            item.isExband = !item.isExband!!
            adapter?.notifyItemChanged(position)
        }
        refresh(refresh)


    }

    private fun refresh(refresh: SwipeRefreshLayout?) {
        DataManager.subscribe(DataManager.init().hotTopic(),
                Consumer {
                    val data = it.data
                    order = data?.last()?.order
                    Log.e(TAG, "order:" + order)
                    adapter?.setList(data)
                    refresh?.isRefreshing = false
                },
                Consumer {
                    Log.e(TAG, "error:", it)
                    refresh?.isRefreshing = false
                })
    }

    private fun loadMore() {
        DataManager.subscribe(DataManager.init().hotTopicMore("$order", 10),
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