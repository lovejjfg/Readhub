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
import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.base.RefreshFragment
import com.lovejjfg.readhub.data.DataManager
import com.lovejjfg.readhub.data.topic.DataItem
import com.lovejjfg.readhub.view.HomeActivity
import com.lovejjfg.readhub.view.recycerview.HotTopicAdapter
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.layout_refresh_recycler.*


/**
 * ReadHub
 * Created by Joe at 2017/7/30.
 */
class HotTopicFragment : RefreshFragment() {

    private var refreshCount: Int = 0
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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        println(" HotTopicFragment onHiddenChanged:: $hidden")
        if (!hidden) {
            checkNews()
        }
    }

    private fun checkNews() {
        refreshCount = 0
        if (adapter?.list?.isEmpty()!!) {
            return
        }
        if (TextUtils.isEmpty(latestOrder)) {
            return
        }
        //todo 如果有置顶操作，似乎获取的数量包含了置顶操作那个 要后续研究了（iPhone X 微信用户破10亿 就这两次有置顶操作 ）
        DataManager.subscribe(this, DataManager.init().newCount(latestOrder!!), Consumer {
            refreshCount = it.count
            if (adapter?.list?.first()!!.isTop) {
                refreshCount--
            }
            if (refreshCount > 0) {
                showHint()
            }
        }, Consumer { it.printStackTrace() })

    }

    private fun showHint() {
        if (mIsVisible) {
            hideNav(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    makeSnack(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            showNav()
                        }
                    })
                }
            })
        } else {
            makeSnack(null)
        }

    }

    private fun makeSnack(callback: BaseTransientBottomBar.BaseCallback<Snackbar>?) {
        if (mSnackBar != null && mSnackBar!!.isShown) {
            mSnackBar!!.dismiss()
        }
        if (mSnackBar == null) {
            @Suppress("DEPRECATION")
            mSnackBar = Snackbar.make(view, String.format(getString(R.string.hot_topic_update), refreshCount), Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(resources.getColor(R.color.colorAccent))
                    .setAction(R.string.refresh, {
                        rv_hot?.scrollToPosition(0)
                        refresh?.isRefreshing = true
                        refresh(refresh)
                        (activity as HomeActivity).selectItem(R.id.navigation_home)
                    })

        }
        if (callback != null) {
            mSnackBar!!.addCallback(callback)
        }
        mSnackBar!!.setText(String.format(getString(R.string.hot_topic_update, refreshCount)))
        mSnackBar!!.show()
    }

    override fun onResume() {
        super.onResume()
//        if (!isHidden) {
        checkNews()
//        }
    }

    override fun refresh(refresh: SwipeRefreshLayout?) {
        mSnackBar?.dismiss()
        DataManager.subscribe(this, DataManager.init().hotTopic(),
                Consumer {
                    if (it.data?.isNotEmpty()!!) {
                        preOrder = latestOrder
                        latestOrder = it.data.first()?.order
                        if (!TextUtils.isEmpty(latestOrder) && latestOrder!!.startsWith("10")) {
                            latestOrder = it.data[1]?.order
                            it.data.first()?.isTop = true
                        } else {
                            it.data.first()?.isTop = false
                        }
                        order = it.data.last()?.order
                        adapter?.setList(it.data)
                        handleAlreadRead(false, it.data, {
                            TextUtils.equals(it?.order, preOrder)
                        })
                    }
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
                    handleAlreadRead(true, adapter?.list!!, {
                        TextUtils.equals(it?.order, preOrder)
                    })
                    Log.i(TAG, "loadMore:order:" + order)
                },
                Consumer {
                    it.printStackTrace()
                    adapter?.loadMoreError()
                })
    }
}