package com.lovejjfg.readhub.view.fragment

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.powerrecycle.holder.PowerHolder
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.base.RelativeItemsDialog
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.data.topic.DataItem
import com.lovejjfg.readhub.data.topic.NewsArrayItem
import com.lovejjfg.readhub.databinding.HolderHotTopicBinding
import com.lovejjfg.readhub.databinding.HolderHotTopicItemBinding
import com.lovejjfg.readhub.view.WebActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

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
        val hotTopicHolder = HotTopicHolder(inflate)
        hotTopicHolder.dataBind = inflate
        return hotTopicHolder

    }

    inner class HotTopicHolder(itemView: HolderHotTopicBinding) : PowerHolder<DataItem>(itemView.root) {

        var dataBind: HolderHotTopicBinding? = itemView

        private var relativeItemsDialog: RelativeItemsDialog? = null


        override fun onBind(t: DataItem?) {
            dataBind?.topic = t
            val rvItem = dataBind?.rvItem
            rvItem?.layoutManager = LinearLayoutManager(context)
            val itemAdapter = HotTopicItemAdapter()
            itemAdapter.setOnItemClickListener { itemView, position, item ->
                val i = Intent(context, WebActivity::class.java)
                i.putExtra(Constants.URL, t?.newsArray!![position]?.mobileUrl)

                context.startActivity(i)
            }
            rvItem?.adapter = itemAdapter
            itemAdapter.setList(t?.newsArray)
            val tvRelative = dataBind?.tvRelative
            Observable.fromIterable(t?.entityRelatedTopics)
                    .map { it.entityName!! }
                    .filter { !TextUtils.isEmpty(it) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .toList()
                    .subscribeBy(onError = {}, onSuccess = {
                        tvRelative?.text = null
                        tvRelative?.append("相关：")
                        tvRelative?.append(TextUtils.join(",", it))
                        tvRelative?.setOnClickListener {
                            if (context is FragmentActivity) {
                                val fragmentManager = (context as FragmentActivity).supportFragmentManager!!
                                if (relativeItemsDialog == null) {
                                    relativeItemsDialog = RelativeItemsDialog()
                                }
                                val bundle = Bundle()
                                bundle.putParcelable(Constants.RELATIVE_ITEMS, t!!.entityRelatedTopics!![0]!!)
                                relativeItemsDialog!!.arguments = bundle
                                relativeItemsDialog!!.show(fragmentManager, "xxxx")
                            }

                        }
                    })
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