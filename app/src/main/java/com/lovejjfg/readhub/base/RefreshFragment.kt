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

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Fragment
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.base.BaseFragment
import com.lovejjfg.readhub.data.topic.DataItem
import com.lovejjfg.readhub.databinding.LayoutRefreshRecyclerBinding
import com.lovejjfg.readhub.utils.RxBus
import com.lovejjfg.readhub.utils.ScrollEvent
import com.lovejjfg.readhub.view.HomeActivity
import io.reactivex.functions.Consumer


/**
 * ReadHub
 * Created by Joe at 2017/7/30.
 */
abstract class RefreshFragment : BaseFragment() {
    protected val TAG = "HotTopicFragment"
    protected var order: String? = null
    protected var binding: LayoutRefreshRecyclerBinding? = null
    protected var adapter: PowerAdapter<DataItem>? = null
    var floatButton: FloatingActionButton? = null
    var navigation: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBus.instance.addSubscription(this, ScrollEvent::class.java,
                Consumer {
                    if (isVisible) {
                        binding?.rvHot?.scrollToPosition(0)
                    }
                },
                Consumer { Log.e(TAG, "error:", it) })

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        navigation = (activity as HomeActivity).navigation
        floatButton = (activity as HomeActivity).floatButton
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
        refresh(refresh)
        println("isVisible:$tag")


        refresh?.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        refresh?.setOnRefreshListener { refresh(refresh) }
        adapter?.setOnItemClickListener { _, position, item ->
            item.isExband = !item.isExband!!
            adapter?.notifyItemChanged(position)
        }
        var isVisible = true
        var isAnimating = false
        rvHot?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView?.layoutManager is LinearLayoutManager) {
                    val first = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    if (first == 0) {
                        navigation?.animate()
                                ?.translationY(0f)
                                ?.setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationStart(animation: Animator?) {
                                        isAnimating = true
                                        floatButton?.hide()
                                    }

                                    override fun onAnimationEnd(animation: Animator?) {
                                        isAnimating = false
                                        isVisible = true
                                        super.onAnimationEnd(animation)
                                    }
                                })
                                ?.start()
                    }

                    if (!isAnimating && dy < 0 && !isVisible) {
                        navigation?.animate()
                                ?.translationY(0f)
                                ?.setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationStart(animation: Animator?) {
//                                        floatButton?.hide()
                                        isAnimating = true
                                    }

                                    override fun onAnimationEnd(animation: Animator?) {
                                        super.onAnimationEnd(animation)
                                        isAnimating = false
                                        isVisible = true
                                    }
                                })
                                ?.start()
                    }
                    if (!isAnimating && dy > 0 && isVisible) {
                        navigation?.animate()
                                ?.translationY(navigation?.height!! + 0.5f)
                                ?.setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationStart(animation: Animator?) {
                                        isAnimating = true
//                                        isVisible = true
                                    }

                                    override fun onAnimationEnd(animation: Animator?) {
                                        isVisible = false
                                        isAnimating = false
                                        floatButton?.show()
                                    }
                                })
                    }
                }
            }
        })
    }

    abstract fun createAdapter(): PowerAdapter<DataItem>?

    abstract fun refresh(refresh: SwipeRefreshLayout?)


    abstract fun loadMore()

    override fun onDestroy() {
        super.onDestroy()
        RxBus.instance.unSubscribe(this)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        println("setUserVisibleHint:" + isVisibleToUser)

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.e("TAG", "onHiddenChanged:$hidden::$tag")
        println("onHiddenChanged:" + hidden)

    }


}