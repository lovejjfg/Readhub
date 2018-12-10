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

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.powerrecycle.holder.PowerHolder
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.data.topic.detail.DetailItems
import com.lovejjfg.readhub.databinding.HolderHotTopicItemBinding
import com.lovejjfg.readhub.databinding.HolderTopicHeaderBinding
import com.lovejjfg.readhub.databinding.HolderTopicTimelineBinding
import com.lovejjfg.readhub.utils.DateUtil
import com.lovejjfg.readhub.utils.inflate
import com.lovejjfg.readhub.view.recycerview.holder.DividerHolder

/**
 * Created by joe on 2017/9/13.
 * lovejjfg@gmail.com
 */
class TopicDetailAdapter : PowerAdapter<DetailItems>() {
    override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): PowerHolder<DetailItems> {
        when (viewType) {
            Constants.TYPE_HEADER -> {
                val itemBinding = DataBindingUtil.inflate<HolderTopicHeaderBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.holder_topic_header,
                    parent,
                    false
                )
                return HeaderHolder(itemBinding)
            }
            Constants.TYPE_NEWS -> {
                val itemBinding = DataBindingUtil.inflate<HolderHotTopicItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.holder_hot_topic_item,
                    parent,
                    false
                )
                return HotTopicItemHolder(itemBinding)
            }
            Constants.TYPE_DIVIDER -> {
                return DividerHolder(parent.inflate(R.layout.layout_divider), false)
            }
            else -> {
                val itemBinding = DataBindingUtil.inflate<HolderTopicTimelineBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.holder_topic_timeline,
                    parent,
                    false
                )
                return TimeLineHolder(itemBinding)
            }

        }
    }

    override fun onViewHolderBind(holder: PowerHolder<DetailItems>, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemViewTypes(position: Int): Int {
        return list[position].type!!
    }

    class HeaderHolder(itemView: HolderTopicHeaderBinding?) : PowerHolder<DetailItems>(itemView!!.root, false) {
        val viewBind = itemView
        override fun onBind(t: DetailItems) {
            viewBind?.detail = t.detail
        }
    }

    class TimeLineHolder(itemView: HolderTopicTimelineBinding?) : PowerHolder<DetailItems>(itemView!!.root) {
        val viewBind = itemView
        override fun onBind(t: DetailItems) {
            val timeLine = t.timeLine
            viewBind?.tvContent?.text = timeLine?.title?.trim()
            viewBind?.tvTime?.text = DateUtil.parseTimeLine(timeLine?.createdAt)
            viewBind?.connector?.setType(t.timeLineType!!)
        }
    }

    inner class HotTopicItemHolder(itemView: HolderHotTopicItemBinding) : PowerHolder<DetailItems>(itemView.root) {
        var itemBinding: HolderHotTopicItemBinding? = itemView
        override fun onBind(t: DetailItems) {
            itemBinding!!.news = t.newsItem
        }
    }
}
