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

package com.lovejjfg.readhub.view.recycerview.holder

import android.text.TextUtils
import android.view.View
import com.lovejjfg.powerrecycle.holder.PowerHolder
import com.lovejjfg.readhub.data.topic.detail.DetailItems
import com.lovejjfg.readhub.databinding.HolderImgParseBinding
import com.lovejjfg.readhub.utils.glide.GlideUtils

/**
 * Created by joe on 2017/9/29.
 * Email: lovejjfg@gmail.com
 */
class ImageParseHolder(parseBinding: HolderImgParseBinding) : PowerHolder<DetailItems>(parseBinding.root, false) {
    var imgBind = parseBinding
    override fun onBind(t: DetailItems?) {
        super.onBind(t)
        GlideUtils.into(t?.img, imgBind.ivContent)
        if (!TextUtils.isEmpty(t?.alt)) {
            imgBind.tvIgAlt.visibility = View.VISIBLE
            imgBind.tvIgAlt.text = t?.alt
        } else {
            imgBind.tvIgAlt.visibility = View.GONE
        }

    }
}