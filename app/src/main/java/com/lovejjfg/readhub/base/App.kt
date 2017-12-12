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

package com.lovejjfg.readhub.base

import android.app.Application
import android.os.Environment
import android.preference.PreferenceManager
import android.util.Log
import com.lovejjfg.readhub.BuildConfig
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.utils.http.ToastUtil
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta


/**
 * ReadHub
 * Created by Joe at 2017/8/25.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val notify = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean("auto_update", true)
        Log.i("APP", "自动更新：$notify")


        val autoDownload = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean("auto_download", false)
        Bugly.init(this, BuildConfig.BUGLY, BuildConfig.IS_DEBUG)

        Log.i("APP", "自动下载：$autoDownload")
        Beta.autoInit = true
        Beta.enableHotfix = false
        Beta.canShowApkInfo = false
        Beta.upgradeDialogLayoutId = R.layout.dialog_update
        Beta.showInterruptedStrategy = false
        Beta.autoCheckUpgrade = notify
        Beta.autoDownloadOnWifi = autoDownload
        Beta.smallIconId = R.mipmap.ic_launcher_foreground
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        Beta.init(this, BuildConfig.IS_DEBUG)

        ToastUtil.initToast(this)

    }
}