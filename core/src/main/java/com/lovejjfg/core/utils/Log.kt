package com.lovejjfg.core.utils

import android.util.Log
import com.lovejjfg.core.BuildConfig

/**
 * Created by joe on 2019-07-02.
 * Email: lovejjfg@gmail.com
 */
fun String.logInfo(tag: String = "TAG") {
    if (BuildConfig.DEBUG) {
        Log.i(tag, this)
    }
}

fun String.printSelf() {
    if (BuildConfig.DEBUG) {
        println(this)
    }
}
