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

import android.Manifest
import android.app.Fragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.base.BaseActivity
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.databinding.ActivityHomeBinding
import com.lovejjfg.readhub.utils.FirebaseUtils.logEvent
import com.lovejjfg.readhub.utils.JumpUitl
import com.lovejjfg.readhub.utils.RxBus
import com.lovejjfg.readhub.utils.SharedPrefsUtil
import com.lovejjfg.readhub.utils.UIUtil
import com.lovejjfg.readhub.utils.event.ScrollEvent
import com.lovejjfg.readhub.view.fragment.DevelopFragment
import com.lovejjfg.readhub.view.fragment.HotTopicFragment
import com.lovejjfg.readhub.view.fragment.TechFragment
import com.tbruyelle.rxpermissions2.RxPermissions

class HomeActivity : BaseActivity() {

    @Suppress("PropertyName")
    val TAG = "HOME"
    private var viewBind: ActivityHomeBinding? = null
    private var hotTopicFragment: Fragment? = null
    private var techFragment: Fragment? = null
    private var developFragment: Fragment? = null
    var navigation: BottomNavigationView? = null
    private var currentId: Int = R.id.navigation_home


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        currentId = item.itemId
        when (item.itemId) {
            R.id.navigation_home -> {
                logEvent(this, getString(R.string.title_home))
                fragmentManager.beginTransaction()
                        .show(hotTopicFragment)
                        .hide(techFragment)
                        .hide(developFragment)
                        .commitAllowingStateLoss()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tech -> {
                logEvent(this, getString(R.string.title_tech))
                fragmentManager.beginTransaction()
                        .show(techFragment)
                        .hide(hotTopicFragment)
                        .hide(developFragment)
                        .commitAllowingStateLoss()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dev -> {
                logEvent(this, getString(R.string.title_dev))
                fragmentManager.beginTransaction()
                        .show(developFragment)
                        .hide(hotTopicFragment)
                        .hide(techFragment)
                        .commitAllowingStateLoss()
                return@OnNavigationItemSelectedListener true
            }
            else -> {
                return@OnNavigationItemSelectedListener false
            }
        }
    }
    private val reSelectListener = BottomNavigationView.OnNavigationItemReselectedListener {
        if (UIUtil.doubleClick()) {
            RxBus.instance.post(ScrollEvent())
        }
    }


    override fun afterCreatedView(savedInstanceState: Bundle?) {
        super.afterCreatedView(savedInstanceState)
        checkPermissions()
        viewBind = DataBindingUtil.setContentView(this, R.layout.activity_home)
        val navigation1 = viewBind?.navigation
        navigation = navigation1
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
                    return@setOnMenuItemClickListener true
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
                    .commitAllowingStateLoss()
        } else {
            hotTopicFragment = fragmentManager.findFragmentByTag(Constants.HOT)
            techFragment = fragmentManager.findFragmentByTag(Constants.TECH)
            developFragment = fragmentManager.findFragmentByTag(Constants.DEV)
            if (currentId != 0) {
                navigation1?.selectedItemId = currentId
            }
        }

    }

    private fun checkPermissions() {
        if (!SharedPrefsUtil.getValue(this, Constants.SHOW_PROMISSION, false)) {
            RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe({
                        SharedPrefsUtil.putValue(this, Constants.SHOW_PROMISSION, true)
                    }, { it.printStackTrace() })
        }
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        if (currentId != 0) {
            outState?.putInt(Constants.TAB_ID, currentId)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        try {
            JumpUitl.backHome(this)
        } catch (e: Exception) {
            super.onBackPressed()
        }

    }

    fun selectItem(navigationId: Int) {
        if (currentId != navigationId) {
            currentId = navigationId
            viewBind?.navigation?.selectedItemId = navigationId
        } else {
            println("相同的id值")
        }
    }


}
