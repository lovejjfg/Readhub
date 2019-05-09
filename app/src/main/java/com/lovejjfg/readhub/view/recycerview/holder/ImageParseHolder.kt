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
import com.lovejjfg.readhub.base.BaseViewHolder
import com.lovejjfg.readhub.data.topic.detail.DetailItems
import com.lovejjfg.readhub.utils.glide.GlideUtils
import kotlinx.android.synthetic.main.holder_img_parse.view.igAlt
import kotlinx.android.synthetic.main.holder_img_parse.view.ivContent

/**
 * Created by joe on 2017/9/29.
 * Email: lovejjfg@gmail.com
 */
class ImageParseHolder(view: View) : BaseViewHolder<DetailItems>(view, false) {
    override fun onBind(t: DetailItems) {
        GlideUtils.into(t.img, itemView.ivContent)
        if (!TextUtils.isEmpty(t.alt)) {
            itemView.igAlt.visibility = View.VISIBLE
            itemView.igAlt.text = t.alt
        } else {
            itemView.igAlt.visibility = View.GONE
        }
    }
}
