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

package com.lovejjfg.readhub.base

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.powerrecycle.holder.PowerHolder
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.data.topic.DataItem
import com.lovejjfg.readhub.data.topic.EntityRelatedTopicsItem
import com.lovejjfg.readhub.databinding.HolderTopicItemBinding
import com.lovejjfg.readhub.databinding.LayoutRecyclerBinding

/**
 * Created by Joe on 2017/5/24.
 * Email lovejjfg@gmail.com
 */

class RelativeItemsDialog : DialogFragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val window = dialog.window
        window?.setWindowAnimations(android.R.style.Animation_Translucent)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val relatedItems = arguments.getParcelable<EntityRelatedTopicsItem>(Constants.RELATIVE_ITEMS)
        val layoutBinding = DataBindingUtil.inflate<LayoutRecyclerBinding>(inflater, R.layout.layout_recycler, container, false)
        val rvHot = layoutBinding.rvHot

        rvHot?.layoutManager = LinearLayoutManager(context)
        val itemAdapter = HotTopicItemAdapter()
        rvHot?.adapter = itemAdapter
        itemAdapter.setList(relatedItems?.data)
        return layoutBinding.root
    }


    inner class HotTopicItemAdapter : PowerAdapter<DataItem>() {
        override fun onViewHolderCreate(parent: ViewGroup?, viewType: Int): PowerHolder<DataItem> {
            val itemBinding = DataBindingUtil.inflate<HolderTopicItemBinding>(LayoutInflater.from(parent?.context), R.layout.holder_topic_item, parent, false)
            val itemHolder = HotTopicItemHolder(itemBinding)
            return itemHolder

        }

        override fun onViewHolderBind(holder: PowerHolder<DataItem>?, position: Int) {
            holder!!.onBind(list[position])
        }

    }

    inner class HotTopicItemHolder(itemView: HolderTopicItemBinding) : PowerHolder<DataItem>(itemView.root) {
        var itemBinding: HolderTopicItemBinding? = itemView
        override fun onBind(t: DataItem?) {
            itemBinding!!.news = t
//            itemBinding?.tvSiteName?.text = t?.
        }

    }

}
