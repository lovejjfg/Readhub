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

import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.powerrecycle.holder.PowerHolder
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.data.Constants.ITEM_MAX_COUNT
import com.lovejjfg.readhub.data.topic.DataItem
import com.lovejjfg.readhub.data.topic.NewsArrayItem
import com.lovejjfg.readhub.databinding.HolderHotTopicBinding
import com.lovejjfg.readhub.databinding.HolderHotTopicItemBinding
import com.lovejjfg.readhub.utils.DateUtil
import com.lovejjfg.readhub.utils.JumpUitl
import com.lovejjfg.readhub.utils.inflate
import com.lovejjfg.readhub.view.recycerview.holder.AlreadyReadHolder
import kotlinx.android.synthetic.main.holder_hot_topic.view.iv_share
import kotlinx.android.synthetic.main.holder_hot_topic.view.tv_publish

/**
 * ReadHub
 * Created by Joe at 2017/7/30.
 */
class HotTopicAdapter : PowerAdapter<DataItem>() {
    override fun onViewHolderBind(holder: PowerHolder<DataItem>, position: Int) {
        holder.onBind(list[position])
    }

    override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): PowerHolder<DataItem> {
        return when (viewType) {
            Constants.TYPE_ALREADY_READ -> {
                AlreadyReadHolder(parent.inflate(R.layout.holder_already_read))
            }
            else -> {
                val inflate = DataBindingUtil.inflate<HolderHotTopicBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.holder_hot_topic,
                    parent,
                    false
                )
                val hotTopicHolder = HotTopicHolder(inflate)
                hotTopicHolder.dataBind = inflate
                hotTopicHolder
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
            if (position >= 0 && position < list.size) {
                list[position].id!!.hashCode().toLong()
            } else {
                super.getItemId(position)
            }
        } catch (e: Exception) {
            super.getItemId(position)
        }
    }

    inner class HotTopicHolder(itemView: HolderHotTopicBinding) : PowerHolder<DataItem>(itemView.root) {

        var dataBind: HolderHotTopicBinding? = itemView

        override fun onBind(t: DataItem) {
            if (dataBind?.topic?.order != t.order) {
                dataBind?.topic = t
                val rvItem = dataBind?.rvItem
                rvItem?.layoutManager = LinearLayoutManager(context)
                val itemAdapter = HotTopicItemAdapter()
                itemAdapter.setOnItemClickListener { _, position, _ ->
                    val mobileUrl = t.newsArray!![position]?.mobileUrl
                    JumpUitl.jumpWeb(context, mobileUrl)
                }
                rvItem?.adapter = itemAdapter
                val array = if (t.newsArray!!.size > ITEM_MAX_COUNT) {
                    t.newsArray.subList(0, ITEM_MAX_COUNT)
                } else {
                    t.newsArray
                }
                itemAdapter.setList(array)
                if (t.extra?.instantView!!) {
                    dataBind?.ivTimeLine?.visibility = View.VISIBLE
                    dataBind?.ivTimeLine?.setOnClickListener {
                        JumpUitl.jumpInstant(context, t.id)
                    }
                } else {
                    dataBind?.ivTimeLine?.visibility = View.GONE
                }
                if (TextUtils.isEmpty(t.id)) {
                    dataBind?.ivShow?.visibility = View.GONE
                } else {
                    dataBind?.ivShow?.visibility = View.VISIBLE
                    dataBind?.ivShow?.setOnClickListener {
                        JumpUitl.jumpTimeLine(context, t.id)
                    }
                }
                itemView.iv_share.setOnClickListener {
                    itemView.performLongClick()
                }
            } else {
                dataBind?.topic = t
            }
            if (t.isTop) {
                itemView.tv_publish?.text = "置顶"
            } else {
                itemView.tv_publish?.text = DateUtil.parseTime(t.createdAt)
            }
        }
    }

    inner class HotTopicItemAdapter : PowerAdapter<NewsArrayItem>() {

        override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): PowerHolder<NewsArrayItem> {
            val itemBinding = DataBindingUtil.inflate<HolderHotTopicItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.holder_hot_topic_item,
                parent,
                false
            )
            return HotTopicItemHolder(itemBinding)
        }

        override fun onViewHolderBind(holder: PowerHolder<NewsArrayItem>, position: Int) {
            holder.onBind(list[position])
        }
    }

    inner class HotTopicItemHolder(itemView: HolderHotTopicItemBinding) : PowerHolder<NewsArrayItem>(itemView.root) {
        private var itemBinding: HolderHotTopicItemBinding? = itemView
        override fun onBind(t: NewsArrayItem) {
            itemBinding!!.news = t
        }
    }
}
