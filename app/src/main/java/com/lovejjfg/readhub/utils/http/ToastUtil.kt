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

package com.lovejjfg.readhub.utils.http

import android.app.Activity
import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Looper
import android.support.annotation.StringRes
import android.widget.Toast
import com.lovejjfg.readhub.base.AppProxy
import com.tencent.bugly.crashreport.CrashReport

/**
 * Created by Joe on 2017/1/4.
 * Email lovejjfg@gmail.com
 */
object ToastUtil {
    private var TOAST: Toast? = null

    fun initToast(context: Context?) {
        if (TOAST == null) {
            TOAST = Toast.makeText(context, "", Toast.LENGTH_SHORT)
        }
    }

    fun showToast(context: Context?, msg: String) {
        showToast(context, msg, Toast.LENGTH_SHORT)
    }

    fun showToast(
        context: Context,
        @StringRes StringID: Int
    ) {

        showToast(context, context.getString(StringID), Toast.LENGTH_SHORT)
    }

    private fun showToast(context: Context?, msg: String, duration: Int) {

        try {
            if (Thread.currentThread() === Looper.getMainLooper().thread) {
                safeShow(context, msg, duration)
            } else {
                if (context != null && context is Activity) {
                    context.runOnUiThread { safeShow(context, msg, duration) }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CrashReport.postCatchedException(e)
        }
    }

    private fun safeShow(context: Context?, msg: String, duration: Int) {
        try {
            if (VERSION.SDK_INT >= VERSION_CODES.N) {
                Toast.makeText(context, msg, duration).show()
            } else {
                initToast(context)
                TOAST?.setText(msg)
                TOAST?.duration = duration
                TOAST?.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CrashReport.postCatchedException(e)
        }
    }
}
