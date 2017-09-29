package com.lovejjfg.readhub.view.widget

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.Config.ARGB_8888
import android.util.AttributeSet
import android.view.View
import com.lovejjfg.readhub.utils.UIUtil

/**
 * Created by joe on 2017/9/13.
 * lovejjfg@gmail.com
 */

class ConnectorView : View {


    private var outR: Int = 0
    private var innerR: Int = 0
    private var transitionY: Int = 0

    constructor(context: Context) : super(context) {}

    private val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rootPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val leakPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val clearPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    enum class Type {
        START, NODE, END, ONLY
    }

    private var type: Type = Type.ONLY
    private var cache: Bitmap? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        outR = UIUtil.dip2px(context, 2.5f)
        innerR = UIUtil.dip2px(context, 2f)
//        transitionY = UIUtil.dip2px(context, 5f)
        iconPaint.color = LIGHT_GREY
        rootPaint.color = ROOT_COLOR
        leakPaint.color = ROOT_COLOR
        clearPaint.color = Color.TRANSPARENT
        clearPaint.xfermode = CLEAR_XFER_MODE
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onDraw(canvas: Canvas) {
        val width = width
        val height = height
        transitionY = (height * 0.5f).toInt()


        if (cache != null && (cache!!.width != width || cache!!.height != height)) {
            cache!!.recycle()
            cache = null
        }

        if (cache == null) {
            cache = Bitmap.createBitmap(width, height, ARGB_8888)

            val cacheCanvas = Canvas(cache!!)

            val halfWidth = width / 2f
            val strokeSize = UIUtil.dip2px(context, 0.8f).toFloat()
            iconPaint.strokeWidth = strokeSize
            when (type) {
                ConnectorView.Type.NODE -> {
                    cacheCanvas.drawLine(halfWidth, 0f, halfWidth, height.toFloat(), iconPaint)
                    cacheCanvas.drawCircle(halfWidth, transitionY.toFloat(), outR.toFloat(), leakPaint)
                }
                ConnectorView.Type.START -> {
                    cacheCanvas.drawLine(halfWidth, transitionY.toFloat(), halfWidth, height.toFloat(), iconPaint)
                    cacheCanvas.drawCircle(halfWidth, transitionY.toFloat(), outR.toFloat(), leakPaint)
                }
                ConnectorView.Type.ONLY -> {
                    cacheCanvas.drawCircle(halfWidth, transitionY.toFloat(), outR.toFloat(), leakPaint)
                }
                else -> {
                    cacheCanvas.drawLine(halfWidth, 0F, halfWidth, transitionY.toFloat(), iconPaint)
                    cacheCanvas.drawCircle(halfWidth, transitionY.toFloat(), outR.toFloat(), leakPaint)
                }
            }
        }
        canvas.drawBitmap(cache!!, 0f, 0f, null)
    }

    fun setType(type: Type) {
        if (type != this.type) {
            this.type = type
            if (cache != null) {
                cache!!.recycle()
                cache = null
            }
        }
        invalidate()
    }

    companion object {

        internal val LIGHT_GREY = 0xFFbababa.toInt()
        internal val ROOT_COLOR = 0xFF84a6c5.toInt()
        internal val LEAK_COLOR = 0xFFb1554e.toInt()

        internal val CLEAR_XFER_MODE = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        init {

        }
    }

}
