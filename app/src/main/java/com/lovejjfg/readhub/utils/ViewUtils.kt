package com.lovejjfg.readhub.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.LeadingMarginSpan
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import java.text.DecimalFormat

/**
 * Created by Joe on 2017/4/2..
 * Email lovejjfg@gmail.com
 */


object ViewUtils {

    fun getScreenLocation(view: View): Point {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return Point((location[0] + view.width * 0.5f).toInt(), (location[1] + view.height * 0.5f).toInt())
    }

    fun updateTotalPrice(tvPrice: TextView, price: String, count: Int, smallSize: Int, normalSize: Int, showUnit: Boolean): Boolean {
        try {
            if (TextUtils.isEmpty(price)) {
                tvPrice.text = null
                return false
            }
            tvPrice.text = null
            val format = formatPrices(price, count)
            if (TextUtils.isEmpty(format)) {
                return false
            }
            val strings = format!!.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (strings.size == 2) {
                val text1 = strings[0]
                val span0 = SpannableString("¥ ")
                span0.setSpan(AbsoluteSizeSpan(smallSize, true), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                val span1 = SpannableString(text1)
                span1.setSpan(AbsoluteSizeSpan(normalSize, true), 0, text1.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                var text2 = strings[1]
                if (text2.length == 1) {
                    text2 = TextUtils.concat(text2, "0").toString()
                }
                val span2 = SpannableString(text2)
                span2.setSpan(AbsoluteSizeSpan(smallSize, true), 0, text2.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                if (showUnit) {
                    tvPrice.append(span0)
                }
                tvPrice.append(span1)
                tvPrice.append(".")
                tvPrice.append(span2)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun updateTotalPrice(tvPrice: TextView, price: String, smallSize: Int, normalSize: Int, showUnit: Boolean) {
        updateTotalPrice(tvPrice, price, 1, smallSize, normalSize, showUnit)
    }

    fun updateTotalPrice(tvPrice: TextView, price: String, showUnit: Boolean) {
        updateSameTotalPrice(tvPrice, price, showUnit)
    }

    private fun updateSameTotalPrice(tvPrice: TextView, price: String, showUnit: Boolean) {
        try {
            if (TextUtils.isEmpty(price)) {
                tvPrice.text = null
            }
            val format = formatPrices(price)
            tvPrice.text = if (showUnit) String.format("¥ %s", format) else format
        } catch (e: Exception) {
            e.printStackTrace()
            tvPrice.text = null
        }

    }

    @JvmOverloads
    fun getFormattedPrice(price: String, formate: String? = null): String? {
        if (TextUtils.isEmpty(price)) {
            return null
        }
        val format = formatPrices(price)
        if (TextUtils.isEmpty(formate)) {
            return format
        }
        return if (TextUtils.isEmpty(formate)) format else String.format(formate!!, format)
    }

    private fun formatPrices(price: String): String? {
        return formatPrices(price, 1)
    }

    //2.6.0 价格单位从「元」到「分」，采用四舍六入的方式。
    fun formatPrices(price: String, count: Int): String? {
        try {
            val df = DecimalFormat("###0.00")
            val currentPrice = java.lang.Double.parseDouble(price)
            val totalPrice = currentPrice * count.toDouble() * 0.01
            return df.format(totalPrice)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            return null
        }

    }

    // return 5 not 5.00
    fun getShortFormattedPrice(price: String): String {
        var s = getFormattedPrice(price, null)
        if (s != null && s.contains(".")) {
            val split = s.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (split.size == 2 && TextUtils.equals(split[1], "00")) {
                s = split[0]
            }
        }
        return s!!
    }

    fun isLaunchedActivity(context: Context, clazz: Class<*>): Boolean {
        try {
            val intent = Intent(context, clazz)
            val cmpName = intent.resolveActivity(context.packageManager)
            var flag = false
            if (cmpName != null) { // 说明系统中存在这个activity
                val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val taskInfoList = am.getRunningTasks(10)
                for (taskInfo in taskInfoList) {
                    if (taskInfo.baseActivity == cmpName) { // 说明它已经启动了
                        flag = true
                        break
                    }
                }
            }
            return flag
        } catch (e: SecurityException) {
            e.printStackTrace()
            return false
        }

    }

    fun calculateMaxLines(textView: TextView) {
        textView.doOnPreDraw {
            val maxLines = textView.height / textView.lineHeight
            textView.maxLines = maxLines
        }
    }

    fun calculateTag1(first: TextView, second: TextView, text: String) {
        first.doOnPreDraw {
            val layout = first.layout
            val lineStart = layout.getLineStart(0)
            val lineEnd = layout.getLineEnd(0)
            val substring = text.substring(lineStart, lineEnd)
            val substring1 = text.substring(lineEnd, text.length)
            Log.e("TAG", "onGlobalLayout: first:" + lineStart + "second:" + lineEnd)
            Log.e("TAG", "onGlobalLayout: 第一行的内容：：" + substring)
            //                first.setText(substring);
            second.text = substring1
        }
    }

    fun calculateTag2(tag: TextView, second: TextView, text: String) {
        val observer = tag.viewTreeObserver
        observer.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val spannableString = SpannableString(text)
                spannableString.setSpan(LeadingMarginSpan.Standard(tag.width, 0), 0, spannableString.length, SpannableString.SPAN_INCLUSIVE_INCLUSIVE)
                second.text = spannableString
                tag.viewTreeObserver.removeOnPreDrawListener(
                        this)
                return false
            }
        })
    }
}
