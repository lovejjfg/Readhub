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

package com.lovejjfg.readhub.utils

import android.text.TextUtils
import android.text.format.DateUtils
import java.text.ParseException

/**
 * Created by joe on 2018/5/30.
 * Email: lovejjfg@gmail.com
 */
fun CharSequence.isTopOrder(): Boolean {
    return this.length > 6
}

fun CharSequence?.empty(): Boolean {
    return this == null || this.isEmpty()
}

fun String.parseTime(): String? {
    if (TextUtils.isEmpty(this)) {
        return null
    }
    try {
        val sdf = DateUtil.initDateFormat()
        val d = sdf.parse(this)
        val s = DateUtils.getRelativeTimeSpanString(d.time, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS)
            .toString()
        if (!TextUtils.isEmpty(s) && s.startsWith("0分钟")) {
            return "刚刚"
        }
        return if (!TextUtils.isEmpty(s) && s.startsWith("0 minutes")) {
            "Just Now"
        } else s
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun String.parseTimeToMillis(): String? {
    return try {
        val sdf = DateUtil.initDateFormat()
        sdf.parse(this).time.toString()
    } catch (e: ParseException) {
        e.printStackTrace()
        null
    }
}

fun String.parseTimeLine(): String? {
    return DateUtil.parseTimeFormate(this)
}
