/*
 * Copyright (c) 2019.  Joe
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

import android.app.Application
import android.content.Context
import android.os.Environment
import android.support.multidex.MultiDex
import android.util.Log
import com.lovejjfg.readhub.BuildConfig
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.utils.http.ToastUtil
import com.lovejjfg.readhub.utils.ioToMain
import com.lovejjfg.shake.ShakerHelper
import com.meituan.android.walle.WalleChannelReader
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.tinker.lib.reporter.DefaultLoadReporter
import io.reactivex.Observable
import java.io.File

/**
 * Created by joe on 2019-06-21.
 * Email: lovejjfg@gmail.com
 */
class AppDelegate(private val application: Application) {

    private var channel: String = "dev"

    init {
        applicationContext = application
        cacheDirectory = File(application.cacheDir, "responses")
    }

    fun onBaseContextAttached(base: Context) {
        MultiDex.install(base)
        channel = WalleChannelReader.getChannel(applicationContext, channel) ?: channel
        if (channel != "googleplay") {
            Log.e("Readhub", "init hot fix.")
            Beta.installTinker(this, object : DefaultLoadReporter(base) {
                override fun onLoadResult(patchDirectory: File?, loadCode: Int, cost: Long) {
                    super.onLoadResult(patchDirectory, loadCode, cost)
                    needRestart = loadCode == 0
                }

                override fun onLoadException(e: Throwable?, errorCode: Int) {
                    super.onLoadException(e, errorCode)
                    CrashReport.postCatchedException(e)
                }
            }, null, null, null, null)
        }
    }

    fun onCreate() {
        ShakerHelper.init(application)
        CrashReport.setIsDevelopmentDevice(application, BuildConfig.IS_DEBUG)

        val strategy = CrashReport.UserStrategy(application)
        Log.e("Readhub", " $channel")
        strategy.appChannel = channel
        Bugly.init(application, BuildConfig.BUGLY, BuildConfig.IS_DEBUG, strategy)
        Beta.autoInit = true
        Beta.canAutoPatch = true
        Beta.canShowApkInfo = false
        Beta.upgradeDialogLayoutId = R.layout.dialog_update
        Beta.showInterruptedStrategy = true
        Beta.autoCheckUpgrade = true
        Beta.autoDownloadOnWifi = false
        Beta.smallIconId = R.mipmap.ic_launcher_foreground
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        Beta.init(application, BuildConfig.IS_DEBUG)
        ToastUtil.initToast(application)
        clearShareCache()
    }

    private fun clearShareCache() {
        val subscribe = Observable.just(File(applicationContext.externalCacheDir, "share"))
            .flatMap {
                Observable.fromIterable(it.list().toList())
            }
            .map {
                val file = File(applicationContext.externalCacheDir, "share${File.separator}$it")
                file.delete()
            }
            .ioToMain()
            .subscribe({ }, { })
    }

    companion object {
        lateinit var cacheDirectory: File
        var needRestart: Boolean = false
        lateinit var applicationContext: Application
    }
}
