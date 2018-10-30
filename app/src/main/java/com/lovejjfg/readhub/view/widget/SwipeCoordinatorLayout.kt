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
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.util.AttributeSet
import android.view.MotionEvent
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
    private var rawX: Float = 0F
    private val path = Path()
    private val arrowPath = Path()
    private val pathPaint = Paint()
    private var animator: ValueAnimator
    private var percent: Float = 0F
    private var arrowLength: Float = 0F
    private var arrowPaint: Paint
    private var xMaxValue = 200F
    private var xDefaultOffset = 50F

    private var callback: Callback? = null
    private val callbackRunnable = Runnable {
        callback?.invoke()
    }

    init {
        xMaxValue = context.dip2px(70f).toFloat()
        xDefaultOffset = context.dip2px(25f).toFloat()
        pathPaint.color = Color.BLACK
        pathPaint.style = Paint.Style.FILL
        pathPaint.isAntiAlias = true

        arrowPaint = Paint(pathPaint)
        arrowPaint.color = Color.WHITE
        arrowPaint.strokeWidth = context.dip2px(2f).toFloat()
        arrowLength = context.dip2px(6f).toFloat()
        arrowPaint.strokeCap = Paint.Cap.ROUND
        arrowPaint.style = Paint.Style.STROKE

        animator = ValueAnimator()
        animator.setObjectValues(1f, 0)
        animator.interpolator = LINEAR_INTERPOLATOR
        animator.duration = 200
        animator.addUpdateListener { animation ->
            val animatedFraction = animation.animatedFraction
            invalidate((1 - animatedFraction) * rawX)
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
        rawX = (event.x - xDefaultOffset) * 0.8f
        yResult = event.y
        val action = event.action
        when (action) {
            MotionEvent.ACTION_UP -> {
                handleUpEvent()
                return true
            }
        }
        invalidate(rawX)
        return true
    }

    private fun handleUpEvent() {
        if (percent >= 1 && callback != null) {
            mBezierPoints = null
            FirebaseUtils.logEvent(context, "滑动关闭", Pair("activity", context::class.java.simpleName))
            invalidate()
            post(callbackRunnable)
        } else {
            if (animator.isRunning) {
                animator.cancel()
            }
            animator.start()
        }
    }

    private fun invalidate(rawX: Float) {
        if (rawX < 0) {
            return
        }
        val min = if (rawX != 0f) Math.min(rawX, xMaxValue) else 0F
        percent = min / xMaxValue
        xResult = INTERPOLATOR.getInterpolation(percent) * xMaxValue
        buildBezierPoints()
        invalidate()
    }

    private fun initControlPoint(min: Float) {
        addControlPoint(0, 0f, yResult - xMaxValue * 1.5f)
        addControlPoint(1, 0f, yResult - xMaxValue * 0.573f)
        addControlPoint(2, min, yResult)
        addControlPoint(3, 0f, yResult + xMaxValue * 0.573f)
        addControlPoint(4, 0f, yResult + xMaxValue * 1.5f)
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
        val points = mBezierPoints ?: return
        pathPaint.alpha = (percent * 210).toInt()
        path.reset()
        for (pointF in points) {
            path.lineTo(pointF.x, pointF.y)
        }
        path.close()
        canvas.drawPath(path, pathPaint)
        if (percent > 0.4) {
            arrowPaint.alpha = 55 + (percent * 200).toInt()
            arrowPath.reset()
            val sin = Math.sin(Math.toRadians((50 + (1 - percent) * 65.0))).toFloat()
            val cos = Math.cos(Math.toRadians((50 + (1 - percent) * 65.0))).toFloat()
            val dy = arrowLength * sin
            val dx = arrowLength * cos
            val x = xResult * 0.2f
            arrowPath.moveTo(x, yResult - dy)
            arrowPath.lineTo(x - dx, yResult)
            arrowPath.lineTo(x, yResult + dy)
            canvas.drawPath(arrowPath, arrowPaint)
        } else {
            arrowPaint.alpha = 55 + (percent * 200).toInt()
            arrowPath.reset()
            val dy = arrowLength * 2.5f * percent
            val x = xResult * 0.2f * 2.5f * percent
            arrowPath.moveTo(x, yResult - dy)
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
        private val INTERPOLATOR = LinearOutSlowInInterpolator()
        private val LINEAR_INTERPOLATOR = FastOutLinearInInterpolator()
    }
}
private typealias Callback = () -> Unit
