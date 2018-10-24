package com.lovejjfg.readhub.view.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.LinearInterpolator
import com.lovejjfg.readhub.utils.FirebaseUtils
import com.lovejjfg.readhub.utils.dip2px
import java.util.ArrayList

@Suppress("unused")
/**
 * Created by joe on 2018/10/13.
 * Email: lovejjfg@gmail.com
 */
class SwipeCoordinatorLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) :
    CoordinatorLayout(context, attrs, defStyleAttr) {
    private var edges: Boolean = false
    private var mBezierPoints: ArrayList<PointF>? = null
    private val mControlPoints = ArrayList<PointF>(5)
    private var xResult: Float = 0F
    private var yResult: Float = 0F
    private val path = Path()
    private val arrowPath = Path()
    private val pathPaint = Paint()
    private var animator: ValueAnimator
    private var percent: Float = 0F
    private var arrowPaint: Paint

    private var callback: Callback? = null

    init {
        pathPaint.color = Color.BLACK
        pathPaint.style = Paint.Style.FILL
        pathPaint.isAntiAlias = true

        arrowPaint = Paint(pathPaint)
        arrowPaint.color = Color.WHITE
        arrowPaint.strokeWidth = context.dip2px(2f).toFloat()
        arrowPaint.strokeCap = Paint.Cap.ROUND
        arrowPaint.style = Paint.Style.STROKE

        animator = ValueAnimator()
        animator.setObjectValues(1f, 0)
        animator.interpolator = LINEAR_INTERPOLATOR
        animator.duration = 200
        animator.addUpdateListener { animation ->
            val animatedFraction = animation.animatedFraction
            xResult *= (1 - animatedFraction)
            invalidate(xResult)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                edges = isEdges(ev)
                return edges
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    private fun isEdges(ev: MotionEvent): Boolean {
        return ev.x < 10
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!edges) {
            return false
        }
        if (animator.isRunning) {
            return false
        }
        val rawX = event.x * 0.8f
        yResult = event.y
        val action = event.action
        when (action) {
            MotionEvent.ACTION_UP -> {
                dispatchCallback()
                if (animator.isRunning) {
                    animator.cancel()
                }
                animator.start()
                return true
            }
        }
        invalidate(rawX)
        return true
    }

    private fun dispatchCallback() {
        if (percent >= 1 && callback != null) {
            FirebaseUtils.logEvent(context, "滑动关闭", Pair("activity", context::class.java.simpleName))
            callback?.invoke()
        }
    }

    private fun invalidate(rawX: Float) {
        val min = if (rawX != 0f) Math.min(rawX, MAX_X_VALUE) else 0F
        percent = min / MAX_X_VALUE
        xResult = INTERPOLATOR.getInterpolation(percent) * MAX_X_VALUE
        buildBezierPoints()
        invalidate()
    }

    private fun initControlPoint(min: Float) {
        addControlPoint(0, 0f, yResult - min * 2)
        addControlPoint(1, 0f, yResult - min * 0.5f)
        addControlPoint(2, min, yResult)
        addControlPoint(3, 0f, yResult + min * 0.5f)
        addControlPoint(4, 0f, yResult + min * 2)
    }

    private fun addControlPoint(pos: Int, x: Float, y: Float) {
        if (pos >= mControlPoints.size) {
            mControlPoints.add(PointF(x, y))
        } else {
            mControlPoints[pos].set(x, y)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mBezierPoints == null) {
            return
        }
        pathPaint.alpha = (percent * 255).toInt()
        arrowPaint.alpha = (percent * 255).toInt()
        path.reset()
        for (pointF in mBezierPoints!!) {
            path.lineTo(pointF.x, pointF.y)
        }
        path.close()
        canvas.drawPath(path, pathPaint)
        if (percent > 0.2) {
            arrowPath.reset()
            val sin = Math.sin(Math.toRadians((50 + (1 - percent) * 50).toDouble()))
            val cos = Math.cos(Math.toRadians((50 + (1 - percent) * 50).toDouble()))
            val dy = (20 * sin).toFloat()
            val dx = (20 * cos).toFloat()
            val x = xResult * 0.25f
            arrowPath.moveTo(x, yResult - dy)
            arrowPath.lineTo(x - dx, yResult)
            arrowPath.lineTo(x, yResult + dy)
            canvas.drawPath(arrowPath, arrowPaint)
        }
    }

    private fun buildBezierPoints() {
        initControlPoint(xResult)
        val order = mControlPoints.size - 1
        for (t in 0 until FRAME) {
            val delta = t * 1.0f / FRAME
            PointFPool.setPointF(t, calculateX(order, 0, delta), calculateY(order, 0, delta))
        }
        mBezierPoints = PointFPool.points
    }

    private fun calculateX(i: Int, j: Int, t: Float): Float {
        return if (i == 1) {
            (1 - t) * mControlPoints[j].x + t * mControlPoints[j + 1].x
        } else (1 - t) * calculateX(i - 1, j, t) + t * calculateX(i - 1, j + 1, t)
    }

    private fun calculateY(i: Int, j: Int, t: Float): Float {
        return if (i == 1) {
            (1 - t) * mControlPoints[j].y + t * mControlPoints[j + 1].y
        } else (1 - t) * calculateY(i - 1, j, t) + t * calculateY(i - 1, j + 1, t)
    }

    fun setOnSwipeBackListener(callback: Callback) {
        this.callback = callback
    }

    private object PointFPool {

        internal val points = ArrayList<PointF>(FRAME)

        internal fun setPointF(pos: Int, x: Float, y: Float) {
            if (pos >= points.size) {
                points.add(PointF(x, y))
            } else {
                points[pos].set(x, y)
            }
        }
    }

    companion object {
        private const val FRAME = 100
        private const val MAX_X_VALUE = 200F
        private val INTERPOLATOR = LinearOutSlowInInterpolator()
        private val LINEAR_INTERPOLATOR = LinearInterpolator()
    }
}
private typealias Callback = () -> Unit
