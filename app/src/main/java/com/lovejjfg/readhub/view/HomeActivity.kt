package com.lovejjfg.readhub.view

import android.app.Fragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.databinding.ActivityHomeBinding
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


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_home -> {
                fragmentManager.beginTransaction()
                        .show(hotTopicFragment)
                        .hide(techFragment)
                        .hide(developFragment)
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                fragmentManager.beginTransaction()
                        .show(techFragment)
                        .hide(hotTopicFragment)
                        .hide(developFragment)
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBind = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
        viewBind?.navigation?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (savedInstanceState == null) {
            hotTopicFragment = HotTopicFragment()
            techFragment = TechFragment()
            developFragment = DevelopFragment()
        } else {
            hotTopicFragment = fragmentManager.findFragmentByTag(Constants.HOT)
            techFragment = fragmentManager.findFragmentByTag(Constants.TECH)
            developFragment = fragmentManager.findFragmentByTag(Constants.DEV)
        }
        fragmentManager.beginTransaction()
                .add(R.id.content, hotTopicFragment, Constants.HOT)
                .add(R.id.content, techFragment, Constants.TECH)
                .add(R.id.content, developFragment, Constants.DEV)
                .hide(techFragment)
                .hide(developFragment)
                .commit()

    }


}
