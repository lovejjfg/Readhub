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
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.View
import android.widget.TextView
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.R.id
import com.lovejjfg.readhub.base.BaseActivity
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.databinding.ActivityHomeBinding
import com.lovejjfg.readhub.utils.AnimUtils
import com.lovejjfg.readhub.utils.FirebaseUtils.logEvent
import com.lovejjfg.readhub.utils.FirebaseUtils.logScreen
import com.lovejjfg.readhub.utils.JumpUitl
import com.lovejjfg.readhub.utils.RxBus
import com.lovejjfg.readhub.utils.SharedPrefsUtil
import com.lovejjfg.readhub.utils.UIUtil
import com.lovejjfg.readhub.utils.event.NoNetEvent
import com.lovejjfg.readhub.utils.event.ScrollEvent
import com.lovejjfg.readhub.view.fragment.BlockChainFragment
import com.lovejjfg.readhub.view.fragment.DevelopFragment
import com.lovejjfg.readhub.view.fragment.HotTopicFragment
import com.lovejjfg.readhub.view.fragment.TechFragment
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_home.appbarLayout
import kotlinx.android.synthetic.main.activity_home.parentContainer
import kotlinx.android.synthetic.main.activity_home.toolbar

class HomeActivity : BaseActivity() {

    @Suppress("PropertyName")
    val TAG = "HOME"
    private var viewBind: ActivityHomeBinding? = null
    private lateinit var hotTopicFragment: Fragment
    private lateinit var techFragment: Fragment
    private lateinit var developFragment: Fragment
    private lateinit var blockChainFragment: Fragment
    lateinit var navigation: BottomNavigationView
    private var currentId: Int = R.id.navigation_home

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        currentId = item.itemId
        when (item.itemId) {
            R.id.navigation_home -> {
                logEvent(this, getString(R.string.title_home))
                logScreen(this, getString(R.string.title_home))
                supportFragmentManager.beginTransaction()
                    .show(hotTopicFragment)
                    .hide(techFragment)
                    .hide(developFragment)
                    .hide(blockChainFragment)
                    .commitAllowingStateLoss()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tech -> {
                logEvent(this, getString(R.string.title_tech))
                logScreen(this, getString(R.string.title_tech))
                supportFragmentManager.beginTransaction()
                    .show(techFragment)
                    .hide(hotTopicFragment)
                    .hide(developFragment)
                    .hide(blockChainFragment)
                    .commitAllowingStateLoss()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dev -> {
                logEvent(this, getString(R.string.title_dev))
                logScreen(this, getString(R.string.title_dev))
                supportFragmentManager.beginTransaction()
                    .show(developFragment)
                    .hide(hotTopicFragment)
                    .hide(techFragment)
                    .hide(blockChainFragment)
                    .commitAllowingStateLoss()
                return@OnNavigationItemSelectedListener true
            }
            else -> {
                logEvent(this, getString(R.string.title_block_chain))
                logScreen(this, getString(R.string.title_block_chain))
                supportFragmentManager.beginTransaction()
                    .show(blockChainFragment)
                    .hide(hotTopicFragment)
                    .hide(techFragment)
                    .hide(developFragment)
                    .commitAllowingStateLoss()
                return@OnNavigationItemSelectedListener true
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
//        checkPermissions()
        addToast()
        logScreen(this, getString(R.string.title_home))
        viewBind = DataBindingUtil.setContentView(this, R.layout.activity_home)
        navigation = viewBind!!.navigation
        val toolbar = viewBind?.toolbar
        toolbar?.setOnClickListener {
            if (UIUtil.doubleClick()) {
                RxBus.instance.post(ScrollEvent())
            }
        }
        toolbar?.inflateMenu(R.menu.home)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.home_setting -> {
                    JumpUitl.jumpSetting(this)
                    return@setOnMenuItemClickListener true
                }
                R.id.home_search -> {
                    goSearch()
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    JumpUitl.jumpAbout(this)
                    return@setOnMenuItemClickListener true
                }
            }
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.setOnNavigationItemReselectedListener(reSelectListener)
        if (savedInstanceState == null) {
            animateToolbar()
            hotTopicFragment = HotTopicFragment()
            techFragment = TechFragment()
            developFragment = DevelopFragment()
            blockChainFragment = BlockChainFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.content, hotTopicFragment, Constants.HOT)
                .add(R.id.content, techFragment, Constants.TECH)
                .add(R.id.content, developFragment, Constants.DEV)
                .add(R.id.content, blockChainFragment, Constants.BLOCK_CHAIN)
                .hide(techFragment)
                .hide(developFragment)
                .hide(blockChainFragment)
                .commitAllowingStateLoss()
        } else {
            hotTopicFragment = supportFragmentManager.findFragmentByTag(Constants.HOT)!!
            techFragment = supportFragmentManager.findFragmentByTag(Constants.TECH)!!
            developFragment = supportFragmentManager.findFragmentByTag(Constants.DEV)!!
            blockChainFragment = supportFragmentManager.findFragmentByTag(Constants.BLOCK_CHAIN)!!
            if (currentId != 0) {
                navigation.selectedItemId = currentId
            }
        }
        initAppbar()
    }

    private var isExpand: Boolean = true
    private fun initAppbar() {
        appbarLayout.addOnOffsetChangedListener { _, verticalOffset ->
            isExpand = verticalOffset == 0
        }
    }

    private fun goSearch() {
        if (!isExpand) {
            appbarLayout.setExpanded(true, false)
            toolbar.post {
                JumpUitl.jumpSearch(this, toolbar)
            }
        } else {
            JumpUitl.jumpSearch(this, toolbar)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUST_CODE_SEARCH) {
            val view = toolbar.findViewById<View>(id.home_search)
            view?.alpha = 1.0F
        }
    }

    private fun addToast() {
        RxBus.instance.addSubscription(this, NoNetEvent::class.java, Consumer {
            showToast(R.string.net_unavailable)
        }, Consumer {
            it.printStackTrace()
        })
    }

    @Suppress("unused")
    private fun checkPermissions() {
        if (!SharedPrefsUtil.getValue(this, Constants.SHOW_PROMISSION, false)) {
            RxPermissions(this).request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
            )
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

    override fun onStart() {
        super.onStart()
        hideNavigation()
    }

    override fun onPause() {
        super.onPause()
        hideNavigation()
    }

    private fun hideNavigation() {
        parentContainer?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE //保证view稳定
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onBackPressed() {
        try {
            JumpUitl.backHome(this)
        } catch (e: Exception) {
            super.onBackPressed()
        }
    }

    fun selectItem(navigationId: Int) {
        currentId = navigationId
        navigation.selectedItemId = navigationId
    }

    private fun animateToolbar() {
        val t = toolbar.getChildAt(0)
        (t as? TextView)?.apply {
            this.alpha = 0f
            this.scaleX = 0.8f
            this.animate()
                .alpha(1f)
                .scaleX(1f)
                .setStartDelay(400)
                .setDuration(1000).interpolator = AnimUtils.getFastOutSlowInInterpolator(context)
        }
    }
}
