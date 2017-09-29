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

package com.lovejjfg.readhub.view

import android.app.Fragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.databinding.ActivityHomeBinding
import com.lovejjfg.readhub.utils.JumpUitl
import com.lovejjfg.readhub.utils.RxBus
import com.lovejjfg.readhub.utils.ScrollEvent
import com.lovejjfg.readhub.utils.UIUtil
import com.lovejjfg.readhub.view.fragment.DevelopFragment
import com.lovejjfg.readhub.view.fragment.HotTopicFragment
import com.lovejjfg.readhub.view.fragment.TechFragment

class HomeActivity : AppCompatActivity() {

    var order: Int? = 0
    val TAG = "HOME"
    var viewBind: ActivityHomeBinding? = null
    var hotTopicFragment: Fragment? = null
    var techFragment: Fragment? = null
    var developFragment: Fragment? = null
    var floatButton: FloatingActionButton? = null
    var navigation: BottomNavigationView? = null
    var mFirebaseAnalytics: FirebaseAnalytics? = null


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                logEvent("热门服务")
                fragmentManager.beginTransaction()
                        .show(hotTopicFragment)
                        .hide(techFragment)
                        .hide(developFragment)
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                logEvent("科技动态")
                fragmentManager.beginTransaction()
                        .show(techFragment)
                        .hide(hotTopicFragment)
                        .hide(developFragment)
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                logEvent("开发资讯")
                fragmentManager.beginTransaction()
                        .show(developFragment)
                        .hide(hotTopicFragment)
                        .hide(techFragment)
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            else -> {
                return@OnNavigationItemSelectedListener false
            }
        }
    }
    private val reSelectListener = BottomNavigationView.OnNavigationItemReselectedListener { item ->
        if (UIUtil.doubleClick()) {
            RxBus.instance.post(ScrollEvent())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseAnalytics?.setCurrentScreen(this, "首页", null)
        viewBind = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
        val navigation1 = viewBind?.navigation
        navigation = navigation1
        floatButton = viewBind?.fab
        val toolbar = viewBind?.toolbar
        toolbar?.setOnClickListener({
            if (UIUtil.doubleClick()) {
                RxBus.instance.post(ScrollEvent())
            }
        })
        toolbar?.inflateMenu(R.menu.home)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.home_setting -> {
                    JumpUitl.jumpSetting(this)
                    return@setOnMenuItemClickListener true

                }
                else -> {
                    JumpUitl.jumpAbout(this)
                    return@setOnMenuItemClickListener false
                }
            }
        }
        navigation1?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation1?.setOnNavigationItemReselectedListener(reSelectListener)
        if (savedInstanceState == null) {
            hotTopicFragment = HotTopicFragment()
            techFragment = TechFragment()
            developFragment = DevelopFragment()
            fragmentManager.beginTransaction()
                    .add(R.id.content, hotTopicFragment, Constants.HOT)
                    .add(R.id.content, techFragment, Constants.TECH)
                    .add(R.id.content, developFragment, Constants.DEV)
                    .hide(techFragment)
                    .hide(developFragment)
                    .commit()
        } else {
            hotTopicFragment = fragmentManager.findFragmentByTag(Constants.HOT)
            techFragment = fragmentManager.findFragmentByTag(Constants.TECH)
            developFragment = fragmentManager.findFragmentByTag(Constants.DEV)
        }
        viewBind?.fab?.setOnClickListener {
            RxBus.instance.post(ScrollEvent())
        }

    }

    fun logEvent(name: String) {
        val params = Bundle()
        params.putString("tab", name)
        mFirebaseAnalytics?.logEvent("tab点击", params)

    }


}
