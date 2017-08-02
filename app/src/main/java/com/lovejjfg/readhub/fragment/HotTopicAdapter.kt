package com.lovejjfg.readhub.fragment

import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.powerrecycle.holder.PowerHolder
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.data.topic.DataItem
import com.lovejjfg.readhub.data.topic.NewsArrayItem
import com.lovejjfg.readhub.databinding.HolderHotTopicBinding
import com.lovejjfg.readhub.databinding.HolderHotTopicItemBinding

/**
 * ReadHub
 * Created by Joe at 2017/7/30.
 */
class HotTopicAdapter : PowerAdapter<DataItem>() {
    override fun onViewHolderBind(holder: PowerHolder<DataItem>?, position: Int) {
        holder?.onBind(list[position])
    }


    override fun onViewHolderCreate(parent: ViewGroup?, viewType: Int): PowerHolder<DataItem> {
        val inflate = DataBindingUtil.inflate<HolderHotTopicBinding>(LayoutInflater.from(parent?.context), R.layout.holder_hot_topic, parent, false)

        val hotTopicHolder = HotTopicHolder(inflate.root)
        hotTopicHolder.dataBind = inflate
        return hotTopicHolder

    }

    inner class HotTopicHolder(itemView: View) : PowerHolder<DataItem>(itemView) {

        var dataBind: HolderHotTopicBinding? = null

        override fun onBind(t: DataItem?) {
            dataBind?.topic = t
            val rvItem = dataBind?.rvItem
            rvItem?.layoutManager = LinearLayoutManager(context)
            val itemAdapter = HotTopicItemAdapter()
            rvItem?.adapter = itemAdapter
            itemAdapter.setList(t?.newsArray)
            itemAdapter.notifyDataSetChanged()

        }
    }

    inner class HotTopicItemAdapter : PowerAdapter<NewsArrayItem>() {
        override fun onViewHolderCreate(parent: ViewGroup?, viewType: Int): PowerHolder<NewsArrayItem> {
            val itemBinding = DataBindingUtil.inflate<HolderHotTopicItemBinding>(LayoutInflater.from(parent?.context), R.layout.holder_hot_topic_item, parent, false)
            val itemHolder = HotTopicItemHolder(itemBinding)
            return itemHolder

        }

        override fun onViewHolderBind(holder: PowerHolder<NewsArrayItem>?, position: Int) {
            holder!!.onBind(list[position])
        }

    }

    inner class HotTopicItemHolder(itemView: HolderHotTopicItemBinding) : PowerHolder<NewsArrayItem>(itemView.root) {
        var itemBinding: HolderHotTopicItemBinding? = itemView
        override fun onBind(t: NewsArrayItem?) {
            itemBinding!!.news = t
        }

    }

}