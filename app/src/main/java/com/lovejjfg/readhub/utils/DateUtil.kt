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

package com.lovejjfg.readhub.utils

import android.text.TextUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * ReadHub
 * Created by Joe at 2017/8/2.
 */
// databinding not support object type
object DateUtil {
    fun initDateFormat(): SimpleDateFormat {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC+08:00")
        return sdf
    }

    fun initDateFormat(format: String): SimpleDateFormat {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC+08:00")
        return sdf
    }

    fun parseTimeFormate(date: String): String? {
        return try {
            val formate = if (isTheSameYear(date)) "MM-dd" else "M-d\nyyyy"
            val sdf = initDateFormat()
            val parse = sdf.parse(date)
            SimpleDateFormat(formate, Locale.getDefault()).format(parse)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    fun isTheSameYear(date1: String): Boolean {
        return try {
            val sdf = initDateFormat()
            val year = SimpleDateFormat("yyyy", Locale.getDefault())
            val d = sdf.parse(date1)
            val d2 = Date()
            val current = year.format(d2)
            val input = year.format(d)
            TextUtils.equals(current, input)
        } catch (e: Exception) {
            false
        }
    }
}
