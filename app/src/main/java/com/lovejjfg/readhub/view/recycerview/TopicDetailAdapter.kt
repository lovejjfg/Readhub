/*
 * Copyright (c) 2017.  Joe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lovejjfg.readhub.view.recycerview

import android.view.View
import android.view.ViewGroup
import com.lovejjfg.powerrecycle.holder.PowerHolder
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.base.BaseAdapter
import com.lovejjfg.readhub.base.BaseViewHolder
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.data.topic.detail.DetailItems
import com.lovejjfg.readhub.utils.DateUtil
import com.lovejjfg.readhub.utils.inflate
import com.lovejjfg.readhub.view.recycerview.holder.DividerHolder
import kotlinx.android.synthetic.main.holder_hot_topic_item.view.itemSiteName
import kotlinx.android.synthetic.main.holder_hot_topic_item.view.itemTitle
import kotlinx.android.synthetic.main.holder_topic_header.view.topicDes
import kotlinx.android.synthetic.main.holder_topic_timeline.view.connector
import kotlinx.android.synthetic.main.holder_topic_timeline.view.timeLineContent
import kotlinx.android.synthetic.main.holder_topic_timeline.view.timeLineDate

/**
 * Created by joe on 2017/9/13.
 * lovejjfg@gmail.com
 */
class TopicDetailAdapter : BaseAdapter<DetailItems>() {
    override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): PowerHolder<DetailItems> {
        return when (viewType) {
            Constants.TYPE_HEADER -> {
                HeaderHolder(parent.inflate(R.layout.holder_topic_header))
            }
            Constants.TYPE_NEWS -> {
                HotTopicItemHolder(parent.inflate(R.layout.holder_hot_topic_item))
            }
            Constants.TYPE_DIVIDER -> {
                DividerHolder(parent.inflate(R.layout.layout_divider))
            }
            else -> {
                TimeLineHolder(parent.inflate(R.layout.holder_topic_timeline))
            }
        }
    }

    override fun onViewHolderBind(holder: PowerHolder<DetailItems>, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemViewTypes(position: Int): Int {
        return list[position].type
    }

    class HeaderHolder(itemView: View) : BaseViewHolder<DetailItems>(itemView, false) {
        override fun onBind(t: DetailItems) {
            val summary = t.detail?.summary?.trim()
            itemView.topicDes.text = summary
        }
    }

    class TimeLineHolder(itemView: View) : BaseViewHolder<DetailItems>(itemView) {
        override fun onBind(t: DetailItems) {
            val timeLine = t.timeLine
            itemView.timeLineContent.text = timeLine?.title?.trim()
            itemView.timeLineDate.text = DateUtil.parseTimeLine(timeLine?.createdAt)
            itemView.connector.setType(t.timeLineType!!)
        }
    }

    class HotTopicItemHolder(itemView: View) : BaseViewHolder<DetailItems>(itemView) {
        override fun onBind(t: DetailItems) {
            val newsItem = t.newsItem ?: return
            itemView.itemTitle.text = newsItem.title
            itemView.itemSiteName.text = newsItem.siteName
        }
    }
}
