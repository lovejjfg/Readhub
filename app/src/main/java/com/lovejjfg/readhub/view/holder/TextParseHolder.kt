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

package com.lovejjfg.readhub.view.holder

import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.lovejjfg.powerrecycle.holder.PowerHolder
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.data.topic.detail.DetailItems
import com.lovejjfg.readhub.utils.JsoupUtils
import com.lovejjfg.readhub.utils.JumpUitl
import com.lovejjfg.readhub.utils.UIUtil


/**
 * Created by joe on 2017/9/28.
 * Email: lovejjfg@gmail.com
 */
class TextParseHolder(itemView: View) : PowerHolder<DetailItems>(itemView, false) {
    //    @BindView(R.id.tv_content)
//    internal var mContent: TextView? = null
//    @BindDimen(R.dimen.padding_20)
//    internal var padding20: Int = 0
//    @BindDimen(R.dimen.padding_50)
//    internal var padding50: Int = 0
    private val JUSTIFY = "justify"
    private val CENTER = "center"
    private val RIGHT = "right"
    private var mContent = itemView as TextView
    private val padding50 = UIUtil.dip2px(itemView.context, 50F)
    private val padding20 = UIUtil.dip2px(itemView.context, 20F)
    override fun onBind(item: DetailItems?) {
        mContent.movementMethod = LinkMovementMethod.getInstance()
        val gravity = item!!.gravity
        //            mContent.setAlign(AlignTextView.Align.ALIGN_LEFT);
        if (TextUtils.isEmpty(gravity)) {
            mContent.gravity = Gravity.START
        } else if (gravity!!.contains(JUSTIFY)) {
            mContent.gravity = Gravity.START
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mContent.breakStrategy = Layout.BREAK_STRATEGY_HIGH_QUALITY
            }
        } else if (gravity.contains(CENTER)) {
            mContent.gravity = Gravity.CENTER
            //                mContent.setAlign(AlignTextView.Align.ALIGN_CENTER);
        } else if (gravity.contains(RIGHT)) {
            mContent.gravity = Gravity.END
        }
        if (TextUtils.isEmpty(item.text)) {
            mContent.text = null
            mContent.visibility = View.GONE
        } else {
            mContent.visibility = View.VISIBLE
            val padding = if (item.isBlockquote) padding50 else padding20
            mContent.setPadding(padding, 0, padding, 0)
            val h1 = JsoupUtils.haveFlag(JsoupUtils.FLAG_H1, item.flag)//17
            val h6 = JsoupUtils.haveFlag(JsoupUtils.FLAG_H6, item.flag)//13
            val builder = SpannableStringBuilder(Html.fromHtml(item.text))
            if (item.map.isNotEmpty()) {
                for (href in item.map) {
                    val start = builder.indexOf(href.key)
                    builder.setSpan(object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            JumpUitl.jumpWeb(widget.context, href.value)
                            Log.d("TAG", "onClick: ...key:+${href.key} ;value: ${href.value}")
                        }

                        override fun updateDrawState(ds: TextPaint?) {
                            super.updateDrawState(ds)
                            ds?.color = mContent.context.resources.getColor(R.color.colorAccent)
                        }
                    }, start, start + href.key.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                }
            }
            mContent.text = builder
            mContent.textSize = (if (h1) 18 else if (h6) 14 else 16).toFloat()
        }

    }
}