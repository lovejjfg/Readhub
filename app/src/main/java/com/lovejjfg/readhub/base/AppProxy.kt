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

package com.lovejjfg.readhub.base

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.preference.PreferenceManager
import android.support.multidex.MultiDex
import android.util.Log
import com.lovejjfg.readhub.BuildConfig
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.utils.http.NetWorkUtils
import com.lovejjfg.readhub.utils.http.ToastUtil
import com.meituan.android.walle.WalleChannelReader
import com.tencent.bugly.Bugly
import com.tencent.bugly.Bugly.applicationContext
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.tinker.loader.app.DefaultApplicationLike
import java.io.File

/**
 * Created by joe on 2018/5/10.
 * Email: lovejjfg@gmail.com
 */
class AppProxy(
    application: Application?,
    tinkerFlags: Int,
    tinkerLoadVerifyFlag: Boolean,
    applicationStartElapsedTime: Long,
    applicationStartMillisTime: Long,
    tinkerResultIntent: Intent?
) : DefaultApplicationLike(
    application,
    tinkerFlags,
    tinkerLoadVerifyFlag,
    applicationStartElapsedTime,
    applicationStartMillisTime,
    tinkerResultIntent

) {

    val TAG = "AppProxy"

    override fun onCreate() {
        super.onCreate()
        mApp = this

        CrashReport.setIsDevelopmentDevice(applicationContext, BuildConfig.IS_DEBUG);
        val notify = PreferenceManager
            .getDefaultSharedPreferences(application)
            .getBoolean("auto_update", true)

        val strategy = CrashReport.UserStrategy(application)
        strategy.appChannel = WalleChannelReader.getChannel(applicationContext, "dev")

        Log.i("APP", "自动更新：$notify")

        val autoDownload = PreferenceManager
            .getDefaultSharedPreferences(application)
            .getBoolean("auto_download", true)
        Bugly.init(application, BuildConfig.BUGLY, BuildConfig.IS_DEBUG, strategy)

        Log.i("APP", "自动下载：$autoDownload")
        Beta.autoInit = true
        Beta.enableHotfix = true
        Beta.canAutoPatch = true
        Beta.canShowApkInfo = false
        Beta.upgradeDialogLayoutId = R.layout.dialog_update
        Beta.showInterruptedStrategy = false
        Beta.autoCheckUpgrade = notify
        Beta.autoDownloadOnWifi = autoDownload
        Beta.smallIconId = R.mipmap.ic_launcher_foreground
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        Beta.init(application, BuildConfig.IS_DEBUG)
        ToastUtil.initToast(application)
        NetWorkUtils.init(application)
        cacheDirectory = File(applicationContext.cacheDir, "responses")
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    override fun onBaseContextAttached(base: Context) {
        super.onBaseContextAttached(base)
        MultiDex.install(base)
        Beta.installTinker(this)
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    fun registerActivityLifecycleCallback(callbacks: Application.ActivityLifecycleCallbacks) {
        application.registerActivityLifecycleCallbacks(callbacks)
    }

    companion object {
        var cacheDirectory: File? = null
        var mApp: AppProxy? = null
    }
}
