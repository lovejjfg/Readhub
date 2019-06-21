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

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.widget.Toolbar
import android.text.TextUtils
import android.view.View
import com.google.firebase.analytics.FirebaseAnalytics
import com.lovejjfg.readhub.R.id
import com.lovejjfg.readhub.R.string
import com.lovejjfg.readhub.base.AppDelegate
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.view.AboutActivity
import com.lovejjfg.readhub.view.InstantActivity
import com.lovejjfg.readhub.view.SearchActivity
import com.lovejjfg.readhub.view.SettingsActivity
import com.lovejjfg.readhub.view.TopicDetailActivity
import com.lovejjfg.readhub.view.WebActivity
import com.tencent.bugly.crashreport.CrashReport

/**
 * ReadHub
 * Created by Joe at 2017/8/5.
 */

object JumpUitl {

    fun jumpWeb(context: Context?, url: String?) {
        try {
            if (context == null) {
                return
            }
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
                context.fastStartActivity(WebActivity::class.java) {
                    it.putExtra(Constants.URL, url)
                }
            } else {
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(context, intent)
            }
        } catch (e: Throwable) {
            CrashReport.postCatchedException(e)
        }
    }

    fun jumpSetting(context: Context?) {
        if (context == null) {
            return
        }
        val intent = Intent(context, SettingsActivity::class.java)
        startActivity(context, intent)
    }

    fun jumpAbout(context: Context?) {
        if (context == null) {
            return
        }
        val intent = Intent(context, AboutActivity::class.java)
        startActivity(context, intent)
    }

    fun jumpTimeLine(context: Context?, id: String?) {
        if (context == null) {
            return
        }
        val intent = Intent(context, TopicDetailActivity::class.java)
        intent.putExtra(Constants.ID, id)
        startActivity(context, intent)
    }

    fun jumpInstant(context: Context?, id: String?) {
        if (context == null) {
            return
        }
        val intent = Intent(context, InstantActivity::class.java)
        intent.putExtra(Constants.ID, id)
        startActivity(context, intent)
    }

    fun backHome(context: Activity?) {
        if (context == null) {
            return
        }
        if (AppDelegate.needRestart) {
            context.finish()
            android.os.Process.killProcess(android.os.Process.myPid())
            return
        }
        val intent = Intent()
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_HOME)
        context.startActivity(intent)
    }

    fun startActivity(context: Context?, intent: Intent) {
        if (context == null) {
            return
        }
        context.startActivity(intent)
    }

    fun jumpSearch(activity: Activity, toolbar: Toolbar) {
        try {
            val searchMenuView = toolbar.findViewById<View>(id.home_search)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                activity, searchMenuView,
                activity.getString(string.transition_search_back)
            ).toBundle()
            val intent = Intent(activity, SearchActivity::class.java)
            activity.startActivityForResult(intent, Constants.REQUST_CODE_SEARCH, options)
        } catch (e: Exception) {
        }
    }
}
