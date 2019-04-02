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

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by joe on 2018/2/7.
 * Email: lovejjfg@gmail.com
 */

inline fun <T> Context.fastStartActivity(clazz: Class<T>, action: ((intent: Intent) -> Unit)) {
    val intent = Intent(this, clazz)
    action(intent)
    this.startActivity(intent)
}

inline fun Context.fastStartActivity(action: (intent: Intent) -> Unit) {
    val intent = Intent()
    action(intent)
    this.startActivity(intent)
}

fun Context.getStatusBarHeight(): Int {
    return try {
        val resources = this.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        resources.getDimensionPixelSize(resourceId)
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}

fun Context.getNavigationBarHeight(): Int {
    return try {
        val resources = this.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        resources.getDimensionPixelSize(resourceId)
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
fun Context.hasNavigationBar(): Boolean {
    var hasNavigationBar = false
    val rs = this.resources
    val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
    if (id > 0) {
        hasNavigationBar = rs.getBoolean(id)
    }
    try {
        val systemPropertiesClass = Class.forName("android.os.SystemProperties")
        val m = systemPropertiesClass.getMethod("get", String::class.java)
        val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
        if ("1" == navBarOverride) {
            hasNavigationBar = false
        } else if ("0" == navBarOverride) {
            hasNavigationBar = true
        }
    } catch (e: Exception) {
    }

    return hasNavigationBar
}

fun Context.dip2px(dpValue: Float): Int {
    val density = this.resources.displayMetrics.density
    return (dpValue * density + 0.5).toInt()
}

fun Context.getScreenWidth(): Int {
    return this.resources.displayMetrics.widthPixels
}

fun Context.getScreenHeight(): Int {
    return this.resources.displayMetrics.heightPixels
}

fun ViewGroup.inflate(@LayoutRes resId: Int, attachParent: Boolean = false): View {
    return LayoutInflater.from(this.context).inflate(resId, this, attachParent)
}
