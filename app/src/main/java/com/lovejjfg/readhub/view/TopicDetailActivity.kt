package com.lovejjfg.readhub.view

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.TextUtils
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.base.BaseActivity
import com.lovejjfg.readhub.base.BaseAdapter
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.data.DataManager
import com.lovejjfg.readhub.data.topic.detail.DetailItems
import com.lovejjfg.readhub.data.topic.detail.TopicDetail
import com.lovejjfg.readhub.utils.JumpUitl
import com.lovejjfg.readhub.utils.UIUtil
import com.lovejjfg.readhub.utils.ioToMain
import com.lovejjfg.readhub.view.recycerview.TopicDetailAdapter
import com.lovejjfg.readhub.view.widget.ConnectorView.Type.END
import com.lovejjfg.readhub.view.widget.ConnectorView.Type.NODE
import com.lovejjfg.readhub.view.widget.ConnectorView.Type.ONLY
import com.lovejjfg.readhub.view.widget.ConnectorView.Type.START
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_topic_detail.appbarLayout
import kotlinx.android.synthetic.main.activity_topic_detail.detailList
import kotlinx.android.synthetic.main.activity_topic_detail.refreshContainer
import kotlinx.android.synthetic.main.activity_topic_detail.toolbar

/**
 * Created by joe on 2017/9/13.
 * lovejjfg@gmail.com
 */
class TopicDetailActivity : BaseActivity() {
    private var id: String = ""
    private lateinit var topicDetailAdapter: BaseAdapter<DetailItems>

    override fun getLayoutRes(): Int {
        return R.layout.activity_topic_detail
    }

    override fun afterCreatedView(savedInstanceState: Bundle?) {
        super.afterCreatedView(savedInstanceState)
        if (handleIntent(intent)) return
        refreshContainer.setOnRefreshListener {
            getData()
        }
        refreshContainer.isRefreshing = true
        getData()
        toolbar.setOnClickListener {
            if (UIUtil.doubleClick()) {
                detailList.smoothScrollToPosition(0)
            }
        }
        detailList.layoutManager = LinearLayoutManager(this)
        topicDetailAdapter = TopicDetailAdapter().apply {
            detailList.adapter = this
            enableLoadMore(false)
        }
        topicDetailAdapter.setOnItemClickListener { _, position, item ->
            when (topicDetailAdapter.getItemViewTypes(position)) {
                Constants.TYPE_NEWS -> {
                    JumpUitl.jumpWeb(this, item.newsItem?.mobileUrl)
                }
                Constants.TYPE_TIMELINE -> {
                    JumpUitl.jumpTimeLine(this, item.timeLine?.id)
                }

            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (handleIntent(intent)) return
        appbarLayout.setExpanded(true, false)
        refreshContainer.isRefreshing = true
        detailList.scrollToPosition(0)
        topicDetailAdapter.clearList()
        getData()
    }

    private fun handleIntent(intent: Intent): Boolean {
        id = intent.getStringExtra(Constants.ID)
        if (TextUtils.isEmpty(id)) {
            return true
        }
        return false
    }

    private fun getData() {
        DataManager.topicDetail(id)
            .flatMap {
                convertItems(it)
            }
            .toList()
            .ioToMain()
            .subscribe({
                refreshContainer.isRefreshing = false
                topicDetailAdapter.setList(it)
                toolbar.title = it[0].detail?.title
            }, {
                it.printStackTrace()
                refreshContainer.isRefreshing = false
                topicDetailAdapter.showError()
                handleError(it)
            })
            .addTo(mDisposables)
    }

    private fun convertItems(topicDetail: TopicDetail): Observable<DetailItems>? {
        return Observable.unsafeCreate<DetailItems> {
            try {
                it.onNext(DetailItems(topicDetail))
                it.onNext(DetailItems())
                val newsArray = topicDetail.newsArray
                if (newsArray != null && newsArray.isNotEmpty()) {
                    for (item in newsArray) {
                        it.onNext(DetailItems(item))
                    }
                    it.onNext(DetailItems())
                }
                val topics = topicDetail.timeline?.topics
                if (topics != null && topics.isNotEmpty()) {
                    if (topics.size == 1) {
                        val items = DetailItems(topics[0])
                        items.timeLineType = ONLY
                        it.onNext(items)
                    } else {
                        topics.forEachIndexed { index, topicsItem ->
                            val items = DetailItems(topicsItem)
                            when (index) {
                                0 -> items.timeLineType = START
                                topics.size - 1 -> items.timeLineType = END
                                else -> items.timeLineType = NODE
                            }
                            it.onNext(items)
                        }
                    }
                }
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }

        }
    }
}
