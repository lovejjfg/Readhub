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

package com.lovejjfg.readhub.view

/**
 * ReadHub
 * Created by Joe at 2017/8/31.
 */

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.TextView
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.utils.UIUtil
import java.lang.IllegalStateException

class PartnerLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = -1) : ViewGroup(context, attrs, defStyleAttr) {
    private var foreground: Drawable? = null
    //显示样式
    private var type: Int = SINGLE_LINE
    //绘制文字最后一行的顶部坐标
    private var lastLineTop: Int = 0
    //绘制文字最后一行的右边坐标
    private var lastLineRight: Float = 0.toFloat()

    private var margin: Int = 0
    private var pGravity: Int = CENTER


    init {
        margin = UIUtil.dip2px(getContext(), 4F)
        val a = context.obtainStyledAttributes(attrs, R.styleable.PartnerLayout)
        val d = a.getDrawable(R.styleable.ForegroundView_android_foreground)
        margin = a.getDimensionPixelOffset(R.styleable.PartnerLayout_PartnerSpace, margin)
        pGravity = a.getInt(R.styleable.PartnerLayout_PartnerGravity, CENTER)
        if (d != null) {
            setForeground(d)
        }
        a.recycle()
        outlineProvider = ViewOutlineProvider.BOUNDS

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
       //todo add padding
        val childCount = childCount
        val w = View.MeasureSpec.getSize(widthMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        if (childCount == 2) {
            val tv: TextView?
            if (getChildAt(0) is TextView) {
                tv = getChildAt(0) as TextView
                if (TextUtils.isEmpty(tv.text)) {
                    //fast return
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
                    return
                }
                if (tv.lineSpacingMultiplier > 1) {
                    throw IllegalStateException("lineSpacingMultiplier is not support here!!")
                }
                initTextParams(tv.text, tv.measuredWidth, tv.paint)
            } else {
                throw RuntimeException("PartnerLayout first child view not a TextView")
            }

            val sencodView = getChildAt(1)

            //测量子view的宽高
            //两个子view宽度相加小于该控件宽度的时候
            if (tv.measuredWidth + sencodView.measuredWidth + margin <= w) {
                //计算高度
                val height = Math.max(tv.measuredHeight, sencodView.measuredHeight)
                //设置该viewgroup的宽高
                setMeasuredDimension(w, height)
                type = SINGLE_LINE
                return
            }
            if (getChildAt(0) is TextView) {
                //最后一行文字的宽度加上第二个view的宽度大于viewgroup宽度时第二个控件换行显示
                if (lastLineRight + sencodView.measuredWidth + margin > w) {
                    setMeasuredDimension(tv.measuredWidth, tv.measuredHeight + sencodView.measuredHeight)
                    type = NEXT_LINE
                    return
                }

                val height = Math.max(tv.measuredHeight, lastLineTop + sencodView.measuredHeight)
                setMeasuredDimension(tv.measuredWidth, height)
                type = MULTI_LINE
            }
        } else {
            throw RuntimeException("PartnerLayout child count must is 2")
        }


    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        if (type == SINGLE_LINE || type == MULTI_LINE) {
            val tv = getChildAt(0) as TextView
            val v1 = getChildAt(1)
            //设置第二个view在Textview文字末尾位置
            tv.layout(0, 0, tv.measuredWidth, tv.measuredHeight)
            //增加默认间距
            val left = lastLineRight.toInt() + margin
            var top = lastLineTop
            //最后一行的高度 注:通过staticLayout得到的行高不准确故采用这种方式
            val lastLineHeight = tv.bottom - tv.paddingBottom - lastLineTop
            //当第二view高度小于单行文字高度时竖直居中显示
            val extra: Int = tv.lineSpacingExtra.toInt()
            if (v1.measuredHeight < lastLineHeight) {
                when (pGravity) {
                    TOP -> {
                        top = lastLineTop + extra
                    }
                    CENTER -> {
                        top = if (type == MULTI_LINE) {
                            (lastLineTop + (lastLineHeight - v1.measuredHeight + extra) * 0.5).toInt()
                        } else {
                            (lastLineTop + (lastLineHeight - v1.measuredHeight) * 0.5).toInt()
                        }
                    }
                    BOTTOM -> {
                        top = lastLineTop + (lastLineHeight - v1.measuredHeight)
                    }
                }

            }
            v1.layout(left, top, left + v1.measuredWidth, top + v1.measuredHeight)
        } else if (type == NEXT_LINE) {
            val v0 = getChildAt(0)
            val v1 = getChildAt(1)
            //设置第二个view换行显示
            v0.layout(0, 0, v0.measuredWidth, v0.measuredHeight)
            v1.layout(0, v0.measuredHeight, v1.measuredWidth, v0.measuredHeight + v1.measuredHeight)
        }
    }


    /**
     * 得到Textview绘制文字的基本信息
     */
    private fun initTextParams(text: CharSequence, maxWidth: Int, paint: TextPaint) {
        val staticLayout = StaticLayout(text, paint, maxWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
        val lineCount = staticLayout.lineCount
        lastLineTop = staticLayout.getLineTop(lineCount - 1)
        lastLineRight = staticLayout.getLineRight(lineCount - 1)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (foreground != null) {
            foreground!!.setBounds(0, 0, w, h)
        }
    }

    override fun hasOverlappingRendering(): Boolean {
        return false
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who === foreground
    }

    override fun jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState()
        if (foreground != null) foreground!!.jumpToCurrentState()
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if (foreground != null && foreground!!.isStateful) {
            foreground!!.state = drawableState
        }
    }

    /**
     * Returns the drawable used as the foreground of this view. The
     * foreground drawable, if non-null, is always drawn on top of the children.
     *
     * @return A Drawable or null if no foreground was set.
     */
    override fun getForeground(): Drawable? {
        return foreground
    }

    /**
     * Supply a Drawable that is to be rendered on top of the contents of this ImageView
     *
     * @param drawable The Drawable to be drawn on top of the ImageView
     */
    override fun setForeground(drawable: Drawable) {
        if (foreground !== drawable) {
            if (foreground != null) {
                foreground!!.callback = null
                unscheduleDrawable(foreground)
            }

            foreground = drawable

            if (foreground != null) {
                foreground!!.setBounds(0, 0, width, height)
                setWillNotDraw(false)
                foreground!!.callback = this
                if (foreground!!.isStateful) {
                    foreground!!.state = drawableState
                }
            } else {
                setWillNotDraw(true)
            }
            invalidate()
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (foreground != null) {
            foreground!!.draw(canvas)
        }
    }

    override fun drawableHotspotChanged(x: Float, y: Float) {
        super.drawableHotspotChanged(x, y)
        if (foreground != null) {
            foreground!!.setHotspot(x, y)
        }
    }

    companion object {

        private val TOP = 0
        private val CENTER = 1
        private val BOTTOM = 2
        //single
        private val SINGLE_LINE = 4
        //mul
        private val MULTI_LINE = 5
        //next line
        private val NEXT_LINE = 6

    }
}


