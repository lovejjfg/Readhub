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

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.lovejjfg.powerrecycle.holder.PowerHolder
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.base.BaseAdapter
import com.lovejjfg.readhub.base.BaseViewHolder
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.data.topic.DataItem
import com.lovejjfg.readhub.utils.DateUtil
import com.lovejjfg.readhub.utils.inflate
import com.lovejjfg.readhub.utils.parseTime
import com.lovejjfg.readhub.view.recycerview.holder.AlreadyReadHolder
import kotlinx.android.synthetic.main.holder_normal_topic.view.topicDes
import kotlinx.android.synthetic.main.holder_normal_topic.view.topicRelative
import kotlinx.android.synthetic.main.holder_normal_topic.view.topicShare
import kotlinx.android.synthetic.main.holder_normal_topic.view.topicTitle

/**
 * ReadHub
 * Created by Joe at 2017/8/5.
 */

class NormalTopicAdapter : BaseAdapter<DataItem>() {
    override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): PowerHolder<DataItem> {
        return when (viewType) {
            Constants.TYPE_ALREADY_READ -> {
                AlreadyReadHolder(parent.inflate(R.layout.holder_already_read))
            }
            else -> {
                return NormalTopicHolder(parent.inflate(R.layout.holder_normal_topic))
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

    override fun onViewHolderBind(holder: PowerHolder<DataItem>, position: Int) {
        holder.onBind(list[position])
    }

    inner class NormalTopicHolder(itemView: View) : BaseViewHolder<DataItem>(itemView) {
        override fun onBind(t: DataItem) {
            itemView.topicTitle.text = t.title
            itemView.topicRelative.text = null
            itemView.topicRelative.requestLayout()
            val text: String? = if (TextUtils.isEmpty(t.authorName)) {
                t.siteName + " · " + t.publishDate?.parseTime()
            } else {
                t.authorName + "/" + t.siteName + " · " + t.publishDate?.parseTime()
            }
            itemView.topicRelative.text = text
            itemView.topicDes.setOnExpandChangeListener {
                t.isExband = it
            }
            itemView.topicDes.setOriginalText(t.summary)
            itemView.topicDes.isExpanded = t.isExband
            itemView.topicDes.setOnClickListener {
                itemView.performClick()
            }
            itemView.topicDes.setOnLongClickListener {
                itemView.performLongClick()
            }
            itemView.topicShare.setOnClickListener {
                itemView.performLongClick()
            }
        }
    }
}

