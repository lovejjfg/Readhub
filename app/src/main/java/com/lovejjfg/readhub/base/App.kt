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
import com.lovejjfg.readhub.BuildConfig
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.crashreport.CrashReport


/**
 * ReadHub
 * Created by Joe at 2017/8/25.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Bugly.init(this, BuildConfig.BUGLY, BuildConfig.IS_DEBUG)
        Beta.autoInit = true
        Beta.enableNotification = false
        Beta.enableHotfix = false
        Beta.showInterruptedStrategy = false
        Beta.autoCheckUpgrade = true
        Beta.tipsDialogLayoutId
        Beta.autoDownloadOnWifi = true
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        Beta.init(this, BuildConfig.IS_DEBUG)

    }
}