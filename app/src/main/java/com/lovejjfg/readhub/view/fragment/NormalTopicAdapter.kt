package com.lovejjfg.readhub.view.fragment

import android.databinding.DataBindingUtil
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.powerrecycle.holder.PowerHolder
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.data.topic.DataItem
import com.lovejjfg.readhub.databinding.HolderNormalTopicBinding
import com.lovejjfg.readhub.utils.DateUtil

/**
 * ReadHub
 * Created by Joe at 2017/8/5.
 */

class NormalTopicAdapter : PowerAdapter<DataItem>() {
    override fun onViewHolderCreate(parent: ViewGroup?, viewType: Int): PowerHolder<DataItem> {
        val topicBinding = DataBindingUtil.inflate<HolderNormalTopicBinding>(LayoutInflater.from(parent?.context), R.layout.holder_normal_topic, parent, false)
        return NormalTopicHolder(topicBinding)
    }

    override fun onViewHolderBind(holder: PowerHolder<DataItem>?, position: Int) {
        holder!!.onBind(list?.get(position))
    }

    inner class NormalTopicHolder(itemView: HolderNormalTopicBinding) : PowerHolder<DataItem>(itemView.root) {
        var itemBinding: HolderNormalTopicBinding? = itemView
        override fun onBind(t: DataItem?) {
            itemBinding!!.topic = t

            val text: String?
            if (TextUtils.isEmpty(t?.authorName)) {
                text = t?.siteName + " · " + DateUtil.parseTime(t?.publishDate)
            } else {
                text = t?.authorName + "/" + t?.siteName + " · " + DateUtil.parseTime(t?.publishDate)

            }
            itemBinding!!.tvRelative.text = text


        }

    }

}

