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

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AtomicFile
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.util.tryWrite
import androidx.view.toBitmap
import com.lovejjfg.powerrecycle.LoadMoreScrollListener
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.data.topic.DataItem
import com.lovejjfg.readhub.databinding.LayoutRefreshRecyclerBinding
import com.lovejjfg.readhub.utils.FirebaseUtils
import com.lovejjfg.readhub.utils.RxBus
import com.lovejjfg.readhub.utils.UIUtil
import com.lovejjfg.readhub.utils.event.ScrollEvent
import com.lovejjfg.readhub.view.HomeActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tencent.bugly.crashreport.CrashReport
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.layout_refresh_recycler.*
import java.io.File


/**
 * ReadHub
 * Created by Joe at 2017/7/30.
 */
abstract class RefreshFragment : BaseFragment() {
    @Suppress("PropertyName")
    protected val TAG = "HotTopicFragment"
    protected var order: String? = null
    protected var latestOrder: String? = null
    protected var binding: LayoutRefreshRecyclerBinding? = null
    protected var adapter: PowerAdapter<DataItem>? = null
    private var navigation: BottomNavigationView? = null
    var refresh: SwipeRefreshLayout? = null
    var mIsVisible = true
    var mIsAnimating = false
    var mSnackBar: Snackbar? = null
    private var mShareDialog: AlertDialog? = null
    private var hintArrays: Array<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hintArrays = resources.getStringArray(R.array.share_hints)
        RxBus.instance.addSubscription(this, ScrollEvent::class.java,
                Consumer {
                    if (isVisible) {
                        binding?.rvHot?.scrollToPosition(0)
                    }
                },
                Consumer { Log.e(TAG, "error:", it) })

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        navigation = (activity as HomeActivity).navigation
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_refresh_recycler, container, false)
        val root = binding?.root
        return root!!
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("view 创建啦：${toString()}")
        val rvHot = binding?.rvHot
        rvHot?.layoutManager = LinearLayoutManager(activity)
        rvHot?.addOnScrollListener(LoadMoreScrollListener(rvHot))
        adapter = createAdapter()
        handleLongClick()
        adapter?.setHasStableIds(true)
        adapter?.setErrorView(UIUtil.inflate(R.layout.layout_empty, rvHot!!))
        adapter?.attachRecyclerView(rvHot!!)
        refresh = binding?.container
        adapter?.setLoadMoreListener {
            if (order != null) {
                loadMore()
            }
        }
        adapter?.totalCount = Int.MAX_VALUE
        println("tag:$tag;;isHidden:$isHidden")
        if (savedInstanceState == null) {
            if (!isHidden && adapter!!.list.isEmpty()) {
                refresh?.isRefreshing = true
                refresh(refresh)
            }
        } else {
            val mList = savedInstanceState.getParcelableArrayList<DataItem>(Constants.DATA)
            adapter?.setList(mList)
        }
        @Suppress("DEPRECATION")
        refresh?.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        refresh?.setOnRefreshListener { refresh(refresh) }
        adapter?.setOnItemClickListener { _, position, item ->
            item.isExband = !item.isExband!!
            adapter?.notifyItemChanged(position)
        }
        rvHot?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mSnackBar != null && mSnackBar?.isShown!!) {
                    mSnackBar?.dismiss()
                    return
                }
                if (recyclerView?.layoutManager is LinearLayoutManager) {
                    val first = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    if (first == 0) {
                        showNav()
                    }

                    if (!mIsAnimating && dy < 0 && !mIsVisible) {
                        showNav()
                    }
                    if (!mIsAnimating && dy > 0 && mIsVisible) {
                        hideNav()
                    }
                }
            }
        })
    }

    private fun handleLongClick() {
        adapter?.setOnItemLongClickListener { _, position, _ ->
            val quick = PreferenceManager
                    .getDefaultSharedPreferences(mContext)
                    .getBoolean(getString(R.string.quick_share), false)
            if (!quick) {
                showShareDialog(position)
            } else {
                shareWithCheck(position)
            }
            return@setOnItemLongClickListener true
        }
    }

    private fun showShareDialog(position: Int) {
        if (mShareDialog == null) {
            mShareDialog = AlertDialog.Builder(mContext!!)
                    .setTitle(getString(R.string.share))
                    .setMessage(getString(R.string.share_hint_default))
                    .setNegativeButton(getString(R.string.not_send), { _, _ ->
                    }).create()
        }
        if (hintArrays != null) {
            val d = Math.random() * 100
            mShareDialog!!.setMessage(hintArrays!![d.toInt() % hintArrays!!.size])
        }
        mShareDialog!!.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.send), { _, _ ->
            shareWithCheck(position)
        })

        mShareDialog!!.show()
    }

    private fun shareWithCheck(position: Int) {
        RxPermissions(activity).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe({
                    if (!it) {
                        showToast(getString(R.string.maybe_fail_hint))
                    }
                    doShare(position)
                }, { it.printStackTrace() })
    }

    private fun doShare(position: Int) {
        try {
            val bitmap = rv_hot.findViewHolderForAdapterPosition(position)?.itemView?.toBitmap(Bitmap.Config.ARGB_8888)
            val file = File(mContext?.externalCacheDir, String.format(getString(R.string.img_name), System.currentTimeMillis().toString()))
            AtomicFile(file).tryWrite {
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
                bitmap?.recycle()
            }
            val uriToImage = Uri.fromFile(file)
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage)
            shareIntent.type = Constants.IMAGE_TYPE
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_news)))
            FirebaseUtils.logEvent(mContext!!, getString(R.string.share), Pair(Constants.NEWS_ID, adapter?.list!![position]?.id))
        } catch (e: Exception) {
            e.printStackTrace()
            CrashReport.postCatchedException(e)
        }
    }

    protected fun showNav() {
        navigation?.animate()
                ?.translationY(0f)
                ?.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        mIsAnimating = true
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        mIsAnimating = false
                        mIsVisible = true
                    }
                })
                ?.start()
    }

    protected fun hideNav() {
        hideNav(null)
    }

    protected fun hideNav(listenerAdapter: AnimatorListenerAdapter?) {
        val animate = navigation?.animate()

        animate
                ?.translationY(navigation?.height!! + 0.5f)
                ?.setListener(object : AnimatorListenerAdapter() {
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
                ?.start()
    }

    abstract fun createAdapter(): PowerAdapter<DataItem>?

    abstract fun refresh(refresh: SwipeRefreshLayout?)


    abstract fun loadMore()

    override fun onResume() {
        super.onResume()
        //refresh time
        if (!isHidden) {
            adapter?.notifyDataSetChanged()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.instance.unSubscribe(this)
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            if (adapter!!.list.isEmpty()) {
                refresh?.isRefreshing = true
                refresh(refresh)
            } else {
                adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        try {
            if (adapter?.list != null && adapter?.list?.isNotEmpty()!!) {
                outState?.putParcelableArrayList(Constants.DATA, ArrayList(adapter?.list))
            }
            mShareDialog?.dismiss()
            super.onSaveInstanceState(outState)
        } catch (e: Exception) {
        }
    }


}