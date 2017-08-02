package com.lovejjfg.readhub.utils;

import android.text.TextUtils;
import android.text.format.DateUtils;

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


}
