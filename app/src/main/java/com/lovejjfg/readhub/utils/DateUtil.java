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

package com.lovejjfg.readhub.utils;

import android.text.TextUtils;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * ReadHub
 * Created by Joe at 2017/8/2.
 */
// databinding not support object type
public class DateUtil {
    public static String parseTime(String date) {
        if (TextUtils.isEmpty(date)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = initDateFormat();
            Date d = sdf.parse(date);
            return DateUtils.getRelativeTimeSpanString(d.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static SimpleDateFormat initDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC+08:00"));
        return sdf;
    }

    private static SimpleDateFormat initDateFormat(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC+08:00"));
        return sdf;
    }

    public static String parseTimeToMillis(String date) {
        try {
            SimpleDateFormat sdf = initDateFormat();
            return String.valueOf(sdf.parse(date).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parseTimeFormate(String date) {
        try {
            String formate = isTheSameYear(date) ? "MM-dd" : "yyyy-M-d";
            SimpleDateFormat sdf = initDateFormat();
            Date parse = sdf.parse(date);
            return new SimpleDateFormat(formate, Locale.getDefault()).format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String parseTimeLine(String date) {
        return parseTimeFormate(date);
    }

    public static boolean isTheSameYear(String date1) {
        try {
            SimpleDateFormat sdf = initDateFormat();
            SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
            Date d = sdf.parse(date1);
            Date d2 = new Date();
            String current = year.format(d2);
            String input = year.format(d);
            return TextUtils.equals(current, input);
        } catch (Exception e) {
            return false;
        }
    }


}
