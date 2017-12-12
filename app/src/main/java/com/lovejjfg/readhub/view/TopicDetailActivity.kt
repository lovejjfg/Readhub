package com.lovejjfg.readhub.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.base.BaseActivity
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.data.DataManager
import com.lovejjfg.readhub.data.topic.detail.DetailItems
import com.lovejjfg.readhub.databinding.ActivityTopicDetailBinding
import com.lovejjfg.readhub.utils.JumpUitl
import com.lovejjfg.readhub.utils.UIUtil
import com.lovejjfg.readhub.view.widget.ConnectorView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by joe on 2017/9/13.
 * lovejjfg@gmail.com
 */
class TopicDetailActivity : BaseActivity() {
    var id: String? = null
    private var topicDetailAdapter: PowerAdapter<DetailItems>? = null
    var toolbar: Toolbar? = null
    private var refresh: SwipeRefreshLayout? = null
    //todo loadmoreActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getStringExtra(Constants.ID)
        if (TextUtils.isEmpty(id)) {
            finish()
            return
        }
        val topicBind = DataBindingUtil.setContentView<ActivityTopicDetailBinding>(this, R.layout.activity_topic_detail)
        refresh = topicBind?.container
        refresh?.setOnRefreshListener {
            getData()
        }
        refresh?.isRefreshing = true
        getData()
        toolbar = topicBind?.toolbar
        topicBind?.toolbar?.setNavigationOnClickListener({ finish() })
        val rvHot = topicBind?.rvDetail
        rvHot?.layoutManager = LinearLayoutManager(this)
        topicDetailAdapter = TopicDetailAdapter()
        topicDetailAdapter?.setErrorView(UIUtil.inflate(R.layout.layout_empty, rvHot!!))
        topicDetailAdapter!!.attachRecyclerView(rvHot!!)
        topicDetailAdapter!!.setOnItemClickListener({ itemView, position, item ->
            when (topicDetailAdapter!!.getItemViewTypes(position)) {
                Constants.TYPE_NEWS -> {
                    JumpUitl.jumpWeb(this, item?.newsItem?.mobileUrl)
                }
//                Constants.TYPE_TIMELINE -> {
//                    JumpUitl.jumpTimeLine(this, item?.timeLine?.id,)
//                }
            }
        })


    }

    private fun getData() {
        val subscribe = DataManager.convert(DataManager.init().topicDetail(id!!))
                .flatMap { topicDetail ->
                    Observable.create<DetailItems> {
                        try {
                            it.onNext(DetailItems(topicDetail))
                            it.onNext(DetailItems())
                            val newsArray = topicDetail.newsArray
                            if (newsArray != null && newsArray.isNotEmpty()) {
                                for (item in newsArray) {
                                    it.onNext(DetailItems(item!!))
                                }
                                it.onNext(DetailItems())
                            }
                            val topics = topicDetail.timeline?.topics
                            if (topics != null && topics.isNotEmpty()) {
                                if (topics.size == 1) {
                                    val items = DetailItems(topics[0]!!)
                                    items.timeLineType = ConnectorView.Type.ONLY
                                    it.onNext(items)
                                } else {
                                    topics.forEachIndexed({ index, topicsItem ->
                                        val items = DetailItems(topicsItem!!)
                                        when (index) {
                                            0 -> items.timeLineType = ConnectorView.Type.START
                                            topics.size - 1 -> items.timeLineType = ConnectorView.Type.END
                                            else -> items.timeLineType = ConnectorView.Type.NODE
                                        }
                                        it.onNext(items)
                                    })
                                }

                            }
                            it.onComplete()
                        } catch (e: Exception) {
                            it.onError(e)
                        }

                    }
                }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    refresh?.isRefreshing = false
                    topicDetailAdapter!!.setList(it)
                    toolbar?.title = it[0]?.detail?.title
                }, {
                    it.printStackTrace()
                    refresh?.isRefreshing = false
                    topicDetailAdapter?.showError()
                    handleError(it)
                })
        subscribe(subscribe)

    }

}