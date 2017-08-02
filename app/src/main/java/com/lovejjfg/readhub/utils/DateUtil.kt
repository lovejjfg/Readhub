package com.lovejjfg.readhub.utils

import android.text.TextUtils
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * ReadHub
 * Created by Joe at 2017/8/1.
 */
object DateUtil {
    fun parseTime(date: String): String? {
        if (TextUtils.isEmpty(date)) {
            return null
        }
        try {
            val sdf = initDateFormat()
            val d = sdf.parse(date)
            return DateUtils.getRelativeTimeSpanString(d.time, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString()
        } catch (e: Exception) {
            return null
        }

    }

    private fun initDateFormat(): SimpleDateFormat {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC+08:00")
        return sdf
    }
}