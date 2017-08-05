package com.lovejjfg.readhub.view.fragment

import android.app.Fragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.data.topic.DataItem
import com.lovejjfg.readhub.databinding.LayoutRefreshRecyclerBinding


/**
 * ReadHub
 * Created by Joe at 2017/7/30.
 */
abstract class RefreshFragment : Fragment() {
    protected val TAG = "HotTopicFragment"
    protected var order: String? = null
    protected var binding: LayoutRefreshRecyclerBinding? = null
    protected var adapter: PowerAdapter<DataItem>? = null
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
        adapter = createAdapter()
        rvHot?.adapter = adapter
        val refresh = binding?.container
        adapter?.setLoadMoreListener {
            if (order != null) {
                loadMore()
            }
        }
        adapter?.totalCount = Int.MAX_VALUE
        refresh?.isRefreshing = true
        refresh?.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        refresh?.setOnRefreshListener { refresh(refresh) }
        adapter?.setOnItemClickListener { _, position, item ->
            item.isExband = !item.isExband!!
            adapter?.notifyItemChanged(position)
        }
        refresh(refresh)
    }

    abstract fun createAdapter(): PowerAdapter<DataItem>?

    abstract fun refresh(refresh: SwipeRefreshLayout?)


    abstract fun loadMore()

}