package com.lovejjfg.readhub.fragment

import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.powerrecycle.holder.PowerHolder
import com.lovejjfg.readhub.data.HotTopic
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.databinding.HolderHotTopicBinding
import com.lovejjfg.readhub.databinding.HolderHotTopicItemBinding
import com.lovejjfg.readhub.utils.DateUtil

/**
 * ReadHub
 * Created by Joe at 2017/7/30.
 */
class HotTopicAdapter : PowerAdapter<HotTopic.Data>() {
    override fun onViewHolderBind(holder: PowerHolder<HotTopic.Data>?, position: Int) {
        holder?.onBind(list[position])
    }


    override fun onViewHolderCreate(parent: ViewGroup?, viewType: Int): PowerHolder<HotTopic.Data> {
        val inflate = DataBindingUtil.inflate<HolderHotTopicBinding>(LayoutInflater.from(parent?.context), R.layout.holder_hot_topic, parent, false)

        val hotTopicHolder = HotTopicHolder(inflate.root)
        hotTopicHolder.dataBind = inflate
        return hotTopicHolder

    }

    inner class HotTopicHolder(itemView: View) : PowerHolder<HotTopic.Data>(itemView) {

        var dataBind: HolderHotTopicBinding? = null

        override fun onBind(t: HotTopic.Data?) {
            dataBind?.topic = t
            val rvItem = dataBind?.rvItem
            val parseTime = DateUtil.parseTime(t?.createdAt!!)
            println("解析后的时间：" + parseTime)
            dataBind?.tvPublish!!.text = parseTime
            rvItem?.layoutManager = LinearLayoutManager(context)
            val itemAdapter = HotTopicItemAdapter()
            rvItem?.adapter = itemAdapter
            itemAdapter.setList(t?.newsArray)
            itemAdapter.notifyDataSetChanged()

        }
    }

    inner class HotTopicItemAdapter : PowerAdapter<HotTopic.Data.NewsArrayBean>() {
        override fun onViewHolderCreate(parent: ViewGroup?, viewType: Int): PowerHolder<HotTopic.Data.NewsArrayBean> {
            val itemBinding = DataBindingUtil.inflate<HolderHotTopicItemBinding>(LayoutInflater.from(parent?.context), R.layout.holder_hot_topic_item, parent, false)
            val itemHolder = HotTopicItemHolder(itemBinding.root)
            itemHolder.itemBinding = itemBinding
            return itemHolder

        }

        override fun onViewHolderBind(holder: PowerHolder<HotTopic.Data.NewsArrayBean>?, position: Int) {
            holder!!.onBind(list[position])
        }

    }

    inner class HotTopicItemHolder(itemView: View) : PowerHolder<HotTopic.Data.NewsArrayBean>(itemView) {
        var itemBinding: HolderHotTopicItemBinding? = null

        override fun onBind(t: HotTopic.Data.NewsArrayBean?) {
            itemBinding!!.news = t
//            println(t?.title)
//            itemBinding!!.tvTitle!!.text = t?.title
//            itemBinding!!.tvSiteName!!.text = t?.siteName


        }

    }

}