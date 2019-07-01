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

package com.lovejjfg.readhub.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.OnScrollListener
import android.util.AtomicFile
import android.util.Log
import android.view.View
import androidx.core.util.tryWrite
import androidx.core.view.toBitmap
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.powerrecycle.manager.FixedLinearLayoutManager
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.data.topic.DataItem
import com.lovejjfg.readhub.utils.FirebaseUtils
import com.lovejjfg.readhub.utils.RxBus
import com.lovejjfg.readhub.utils.canScrollUp
import com.lovejjfg.readhub.utils.event.ScrollEvent
import com.lovejjfg.readhub.view.HomeActivity
import com.tencent.bugly.crashreport.CrashReport
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.layout_refresh_recycler.refreshContainer
import kotlinx.android.synthetic.main.layout_refresh_recycler.topicList
import java.io.File

/**
 * ReadHub
 * Created by Joe at 2017/7/30.
 */
abstract class RefreshFragment : BaseFragment() {
    @Suppress("PropertyName")
    protected var order: String? = null
    protected var latestOrder: String? = null
    protected var preLatestOrder: String? = null
    protected lateinit var adapter: PowerAdapter<DataItem>
    private lateinit var navigation: BottomNavigationView
    var mIsVisible = true
    var mIsAnimating = false
    private var mShareDialog: AlertDialog? = null
    private lateinit var hintArrays: Array<String>

    override fun beforeCreate(savedInstanceState: Bundle?) {
        hintArrays = resources.getStringArray(R.array.share_hints)
        RxBus.instance.addSubscription(this, ScrollEvent::class.java,
            Consumer {
                if (isVisible) {
                    if (topicList.canScrollUp()) {
                        topicList.scrollToPosition(0)
                    } else {
                        doRefresh()
                    }
                }
            },
            Consumer { Log.e(TAG, "error:", it) })
    }

    override fun getLayoutRes(): Int {
        return R.layout.layout_refresh_recycler
    }

    override fun afterCreatedView(savedInstanceState: Bundle?) {
        navigation = (activity as HomeActivity).navigation
        if (savedInstanceState != null) {
            latestOrder = savedInstanceState.getString(Constants.CURRENT_ID)
            order = savedInstanceState.getString(Constants.LASTED_ID)
        }
        handleView(savedInstanceState)
    }

    private fun handleView(savedInstanceState: Bundle?) {
        println("view 创建啦：${toString()}")
        topicList.layoutManager = FixedLinearLayoutManager(activity)
        adapter = createAdapter()
        handleLongClick()
        adapter.setHasStableIds(true)
        adapter.setLoadMoreListener {
            if (order != null) {
                loadMore()
            } else {
                adapter.loadMoreError()
            }
        }
        adapter.totalCount = Int.MAX_VALUE
        topicList.adapter = adapter
        println("tag:$tag;;isHidden:$isHidden")
        initDataOrRefresh(savedInstanceState)
        @Suppress("DEPRECATION")
        refreshContainer.setOnRefreshListener { refresh(refreshContainer) }
        adapter.setOnItemClickListener { _, position, item ->
            item.isExband = !item.isExband
            adapter.notifyItemChanged(position)
        }
        topicList.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                hideNavigation()
                if (mSnackBar?.isShown == true) {
                    mSnackBar?.dismiss()
                    return
                }
                if (recyclerView.layoutManager is LinearLayoutManager) {
                    val first =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    if (first == 0) {
                        showNav()
                        return
                    }
                }
                if (!mIsAnimating && dy < 0 && !mIsVisible) {
                    showNav()
                    return
                }
                if (!mIsAnimating && dy > 0 && mIsVisible) {
                    hideNav()
                    return
                }
            }
        })
    }

    protected open fun saveListData(): Boolean {
        return false
    }

    private fun hideNavigation() {
        refreshContainer.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE //保证view稳定
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    private fun initDataOrRefresh(savedInstanceState: Bundle?) {
        try {
            if (savedInstanceState == null) {
                doRefreshWithCheck()
            } else {
                val mList = savedInstanceState.getParcelableArrayList<DataItem>(Constants.DATA)
                if (mList != null && mList.isNotEmpty()) {
                    adapter.setList(mList)
                } else {
                    doRefreshWithCheck()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "initDataOrRefresh() : ", e)
            doRefresh()
        }
    }

    private fun doRefreshWithCheck() {
        if (!isHidden && adapter.list.isEmpty()) {
            doRefresh()
        }
    }

    private fun handleLongClick() {
        adapter.setOnItemLongClickListener { _, position, _ ->
            val quick = try {
                PreferenceManager
                    .getDefaultSharedPreferences(mContext)
                    .getBoolean(getString(R.string.quick_share), false)
            } catch (e: Exception) {
                false
            }
            if (!quick) {
                showShareDialog(position)
            } else {
                shareWithCheck(position)
            }
            return@setOnItemLongClickListener true
        }
    }

    private fun showShareDialog(position: Int) {
        val context = mContext ?: return
        if (mShareDialog == null) {
            mShareDialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.share))
                .setMessage(getString(R.string.share_hint_default))
                .setNegativeButton(getString(R.string.not_send)) { _, _ ->
                }
                .setOnDismissListener {
                    hideNavigation()
                }
                .setOnCancelListener {
                    hideNavigation()
                }
                .create()
        }
        val d = Math.random() * 100
        mShareDialog?.setMessage(hintArrays[d.toInt() % hintArrays.size])
        mShareDialog?.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.send)) { _, _ ->
            shareWithCheck(position)
        }
        if (activity?.isFinishing == false) {
            mShareDialog?.show()
        }
    }

    private fun shareWithCheck(position: Int) {
        doShare(position)
    }

    private fun doShare(position: Int) {
        try {
            val context = mContext ?: return
            val itemView = topicList.findViewHolderForAdapterPosition(position)?.itemView ?: return
            itemView.findViewById<View>(R.id.topicShare)?.visibility = View.INVISIBLE
            val bitmap = itemView.toBitmap(Bitmap.Config.ARGB_8888)
            itemView.findViewById<View>(R.id.topicShare)?.visibility = View.VISIBLE
            val file = File(
                context.externalCacheDir,
                "share" + File.separator + String.format(
                    getString(R.string.img_name),
                    System.currentTimeMillis().toString()
                )
            )
            AtomicFile(file).tryWrite {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                bitmap.recycle()
            }

            val uriToImage: Uri
            uriToImage = if (VERSION.SDK_INT <= VERSION_CODES.M) {
                Uri.fromFile(file)
            } else {
                FileProvider.getUriForFile(
                    context, context.packageName + ".fileProvider", file
                )
            }
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage)
            shareIntent.type = Constants.IMAGE_TYPE
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_news)))
            FirebaseUtils.logEvent(
                context,
                Constants.SHARE,
                Pair(Constants.NEWS_ID, adapter.list[position]?.id)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            CrashReport.postCatchedException(e)
            showToast(getString(R.string.share_error))
        }
    }

    protected fun showNav() {
        navigation.animate()
            .translationY(0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    mIsAnimating = true
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    mIsAnimating = false
                    mIsVisible = true
                }
            })
            .start()
    }

    protected fun hideNav() {
        hideNav(null)
    }

    protected fun hideNav(listenerAdapter: AnimatorListenerAdapter?) {
        navigation.animate()
            .translationY(navigation.height + 0.5f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    mIsAnimating = true
                    listenerAdapter?.onAnimationStart(animation)
                }

                override fun onAnimationEnd(animation: Animator?) {
                    mIsAnimating = false
                    mIsVisible = false
                    listenerAdapter?.onAnimationEnd(animation)
                }
            })
            .start()
    }

    abstract fun createAdapter(): PowerAdapter<DataItem>

    abstract fun refresh(refresh: SwipeRefreshLayout?)

    abstract fun loadMore()

    override fun onResume() {
        super.onResume()
        //refresh time
        if (!isHidden) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.instance.unSubscribe(this)
        mSnackBar = null
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        println("$this hiddenChange: $hidden")
        if (!hidden) {
            if (adapter.list.isEmpty()) {
                doRefresh()
            } else {
                adapter.notifyDataSetChanged()
            }
        }
    }

    protected fun doRefresh() {
        if (refreshContainer.isRefreshing) {
            return
        }
        refreshContainer.isRefreshing = true
        refresh(refreshContainer)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        try {
            saveData(outState)
            mShareDialog?.dismiss()
            super.onSaveInstanceState(outState)
        } catch (e: Exception) {
        }
    }

    private fun saveData(outState: Bundle?) {
        if (saveListData() && adapter.list?.isNotEmpty() == true) {
            outState?.putParcelableArrayList(Constants.DATA, ArrayList(adapter.list))
            outState?.putString(Constants.CURRENT_ID, latestOrder)
            outState?.putString(Constants.LASTED_ID, order)
        }
    }

    protected fun handleAlreadyRead(
        loadMore: Boolean,
        data: List<DataItem>,
        fromCache: Boolean = false,
        check: (item: DataItem?) -> Boolean
    ) {
        println("currentId:$latestOrder;;preId::$preLatestOrder")
        try {
            val first = data.firstOrNull {
                !it.isTop && check(it)
            }
            println("头条id:${first?.id}")
            if (first != null) {
                val indexOf = data.indexOf(first)
                if (indexOf < 0) {
                    return
                }
                // no more
                if (indexOf == 0 || (data[indexOf - 1].isTop)) {
                    println("没有新的更新：$indexOf")
                    if (!loadMore && !fromCache) {
                        removeReadAndHint()
                    }
                } else {
                    // 插入 没有更多
                    val removeReadPosition = adapter.findFirstPositionOfType(Constants.TYPE_ALREADY_READ)
                    println("没有更多的位置：$removeReadPosition")
                    if (removeReadPosition + 1 != indexOf) {
                        val hintCount = if (data.first().isTop) indexOf - 1 else indexOf
                        println("插入更新位置：$indexOf")
                        removeRead()
                        showToast(String.format(getString(R.string.update_news_with_count), hintCount))
                        adapter.insertItem(indexOf, DataItem())
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("RefreshFragment", "handleAlreadyRead", e)
            CrashReport.postCatchedException(e)
        }
    }

    private fun removeReadAndHint() {
        removeRead()
        showToast(getString(R.string.wait_away))
    }

    private fun removeRead(): Int {
        return try {
            val position = adapter.findFirstPositionOfType(Constants.TYPE_ALREADY_READ)
            adapter.removeItem(position)
            return position
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    companion object {
        @Volatile
        var mSnackBar: Snackbar? = null
    }
}
