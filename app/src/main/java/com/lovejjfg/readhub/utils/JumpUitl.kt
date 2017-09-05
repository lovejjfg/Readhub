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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import com.google.firebase.analytics.FirebaseAnalytics
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.view.AboutActivity
import com.lovejjfg.readhub.view.SettingsActivity
import com.lovejjfg.readhub.view.WebActivity


/**
 * ReadHub
 * Created by Joe at 2017/8/5.
 */

object JumpUitl {

    fun jumpWeb(context: Context, url: String?) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        val bundle = Bundle()
        bundle.putString("链接", url)
        FirebaseAnalytics.getInstance(context).logEvent("点击", bundle)
        val default = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean("browser_use", false)

        if (!default) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(Constants.URL, url)
            context.startActivity(intent)
        } else {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }
    }

    fun jumpSetting(context: Context) {
        val intent = Intent(context, SettingsActivity::class.java)
        context.startActivity(intent)
    }

    fun jumpAbout(context: Context) {
        val intent = Intent(context, AboutActivity::class.java)
        context.startActivity(intent)
    }
}
