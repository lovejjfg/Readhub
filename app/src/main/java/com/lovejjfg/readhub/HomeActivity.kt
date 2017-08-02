package com.lovejjfg.readhub

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.lovejjfg.readhub.databinding.ActivityHomeBinding
import com.lovejjfg.readhub.fragment.HotTopicFragment

class HomeActivity : AppCompatActivity() {

    var order: Int? = 0
    val TAG = "HOME"
    var viewBind: ActivityHomeBinding? = null


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
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
        fragmentManager.beginTransaction().replace(R.id.content, HotTopicFragment()).commit()

    }


}
