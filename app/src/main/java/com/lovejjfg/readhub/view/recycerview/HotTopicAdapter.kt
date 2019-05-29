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

package com.lovejjfg.readhub.view.recycerview

import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.lovejjfg.powerrecycle.holder.PowerHolder
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.base.BaseAdapter
import com.lovejjfg.readhub.base.BaseViewHolder
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.data.Constants.ITEM_MAX_COUNT
import com.lovejjfg.readhub.data.topic.DataItem
import com.lovejjfg.readhub.data.topic.NewsArrayItem
import com.lovejjfg.readhub.utils.JumpUitl
import com.lovejjfg.readhub.utils.inflate
import com.lovejjfg.readhub.utils.parseTime
import com.lovejjfg.readhub.view.recycerview.holder.AlreadyReadHolder
import kotlinx.android.synthetic.main.holder_hot_topic.view.ivShare
import kotlinx.android.synthetic.main.holder_hot_topic.view.ivShow
import kotlinx.android.synthetic.main.holder_hot_topic.view.ivTimeLine
import kotlinx.android.synthetic.main.holder_hot_topic.view.parentContainer
import kotlinx.android.synthetic.main.holder_hot_topic.view.topicDes
import kotlinx.android.synthetic.main.holder_hot_topic.view.topicItemList
import kotlinx.android.synthetic.main.holder_hot_topic.view.topicPublish
import kotlinx.android.synthetic.main.holder_hot_topic.view.topicTitle
import kotlinx.android.synthetic.main.holder_hot_topic_item.view.itemSiteName
import kotlinx.android.synthetic.main.holder_hot_topic_item.view.itemTitle

/**
 * ReadHub
 * Created by Joe at 2017/7/30.
 */
class HotTopicAdapter : BaseAdapter<DataItem>() {
    override fun onViewHolderBind(holder: PowerHolder<DataItem>, position: Int) {
        holder.onBind(list[position])
    }

    override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): PowerHolder<DataItem> {
        return when (viewType) {
            Constants.TYPE_ALREADY_READ -> {
                AlreadyReadHolder(parent.inflate(R.layout.holder_already_read))
            }
            else -> {
                HotTopicHolder(parent.inflate(R.layout.holder_hot_topic))
            }
        }
    }

    override fun getItemViewTypes(position: Int): Int {
        if (TextUtils.isEmpty(list[position].id)) {
            return Constants.TYPE_ALREADY_READ
        }
        return super.getItemViewTypes(position)
    }

    override fun getItemId(position: Int): Long {
        return try {
            val id = list[position].id
            id?.hashCode()?.toLong() ?: super.getItemId(position)
        } catch (e: Exception) {
            super.getItemId(position)
        }
    }

    class HotTopicHolder(itemView: View) : BaseViewHolder<DataItem>(itemView) {
        private val itemAdapter: HotTopicItemAdapter

        init {
            itemView.topicItemList.layoutManager = LinearLayoutManager(context)
            itemAdapter = HotTopicItemAdapter().apply {
                enableLoadMore(false)
                itemView.topicItemList.adapter = this
            }
        }

        private var dataItem: DataItem? = null
        override fun onBind(t: DataItem) {
            itemView.parentContainer.cardElevation = if (t.isExband) 20f else 4f
            if (dataItem?.order != t.order) {
                dataItem = t
                setRelativeItems(t, itemAdapter)
                if (t.extra?.instantView == true) {
                    itemView.ivTimeLine.visibility = View.VISIBLE
                    itemView.ivTimeLine.setOnClickListener {
                        JumpUitl.jumpInstant(context, t.id)
                    }
                } else {
                    itemView.ivTimeLine.visibility = View.GONE
                }
                if (TextUtils.isEmpty(t.id)) {
                    itemView.ivShow.visibility = View.GONE
                } else {
                    itemView.ivShow.visibility = View.VISIBLE
                    itemView.ivShow.setOnClickListener {
                        JumpUitl.jumpTimeLine(context, t.id)
                    }
                }
                itemView.ivShare.setOnClickListener {
                    itemView.performLongClick()
                }
                handleView(t)
            } else {
                dataItem = t
            }
            if (t.isTop) {
                itemView.topicPublish.text = "置顶"
            } else {
                itemView.topicPublish.text = t.createdAt?.parseTime()
            }
        }

        private fun setRelativeItems(
            t: DataItem,
            itemAdapter: HotTopicItemAdapter
        ) {
            val newsArray: List<NewsArrayItem> = t.newsArray ?: return
            itemAdapter.setOnItemClickListener { _, position, _ ->
                val mobileUrl = newsArray[position].mobileUrl
                JumpUitl.jumpWeb(context, mobileUrl)
            }

            val array = if (newsArray.size > ITEM_MAX_COUNT) {
                newsArray.subList(0, ITEM_MAX_COUNT)
            } else {
                newsArray
            }
            itemAdapter.setList(array)
        }

        private fun handleView(t: DataItem) {
            val title = t.title?.trim()
            itemView.topicTitle.text = title

            itemView.topicDes.apply {
                val summary = t.summary?.trim()
                if (TextUtils.isEmpty(summary)) {
                    isGone = true
                } else {
                    isGone = false
                    text = summary
                    maxLines = if (t.isExband) 1000 else 3
                }
            }
            itemView.topicItemList.isVisible = t.isExband
        }
    }

    class HotTopicItemAdapter : BaseAdapter<NewsArrayItem>() {

        override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): PowerHolder<NewsArrayItem> {
            return HotTopicItemHolder(parent.inflate(R.layout.holder_hot_topic_item))
        }

        override fun onViewHolderBind(holder: PowerHolder<NewsArrayItem>, position: Int) {
            holder.onBind(list[position])
        }
    }

    class HotTopicItemHolder(itemView: View) : BaseViewHolder<NewsArrayItem>(itemView) {
        override fun onBind(t: NewsArrayItem) {
            itemView.itemTitle.text = t.title
            itemView.itemSiteName.text = t.siteName
        }
    }
}
