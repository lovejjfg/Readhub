package com.lovejjfg.readhub.view.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View
import com.lovejjfg.readhub.utils.dip2px
import com.lovejjfg.readhub.view.widget.ConnectorView.Type.NODE
import com.lovejjfg.readhub.view.widget.ConnectorView.Type.ONLY
import com.lovejjfg.readhub.view.widget.ConnectorView.Type.START

/**
 * Created by joe on 2017/9/13.
 * lovejjfg@gmail.com
 */

class ConnectorView : View {

    private var outR: Int = 0
    private var innerR: Int = 0
    private var transitionY: Int = 0

    constructor(context: Context) : super(context)

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

        outR = context.dip2px(2.5f)
        innerR = context.dip2px(2f)
//        transitionY = UIUtil.dip2px(context, 5f)
        iconPaint.color = LIGHT_GREY
        rootPaint.color = ROOT_COLOR
        leakPaint.color = ROOT_COLOR
        clearPaint.color = Color.TRANSPARENT
        clearPaint.xfermode = CLEAR_XFER_MODE
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        val width = width
        val height = height
        transitionY = (height * 0.5f).toInt()


        if (cache?.width != width || cache?.height != height) {
            cache?.recycle()
            cache = null
        }
        val cache = initCache(width, height)
        canvas.drawBitmap(cache, 0f, 0f, null)
    }

    private fun initCache(width: Int, height: Int): Bitmap {
        return this.cache ?: Bitmap.createBitmap(width, height, ARGB_8888).apply {
            cache = this
            val cacheCanvas = Canvas(this)

            val halfWidth = width / 2f
            val strokeSize = context.dip2px(0.8f).toFloat()
            iconPaint.strokeWidth = strokeSize
            when (type) {
                NODE -> {
                    cacheCanvas.drawLine(halfWidth, 0f, halfWidth, height.toFloat(), iconPaint)
                    cacheCanvas.drawCircle(halfWidth, transitionY.toFloat(), outR.toFloat(), leakPaint)
                }
                START -> {
                    cacheCanvas.drawLine(halfWidth, transitionY.toFloat(), halfWidth, height.toFloat(), iconPaint)
                    cacheCanvas.drawCircle(halfWidth, transitionY.toFloat(), outR.toFloat(), leakPaint)
                }
                ONLY -> {
                    cacheCanvas.drawCircle(halfWidth, transitionY.toFloat(), outR.toFloat(), leakPaint)
                }
                else -> {
                    cacheCanvas.drawLine(halfWidth, 0F, halfWidth, transitionY.toFloat(), iconPaint)
                    cacheCanvas.drawCircle(halfWidth, transitionY.toFloat(), outR.toFloat(), leakPaint)
                }
            }
        }
    }

    fun setType(type: Type) {
        if (type != this.type) {
            this.type = type
            cache?.recycle()
            cache = null
        }
        invalidate()
    }

    companion object {
        const val LIGHT_GREY = 0xFFbababa.toInt()
        const val ROOT_COLOR = 0xFF84a6c5.toInt()

        val CLEAR_XFER_MODE = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
}
