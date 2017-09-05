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

import android.app.Fragment
import android.os.Bundle

/**
 * Created by Joe on 2016/10/13.
 * Email lovejjfg@gmail.com
 */

abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragments(savedInstanceState, this)
    }


    fun initFragments(savedInstanceState: Bundle?, fragment: BaseFragment) {
        if (savedInstanceState == null) {
            return
        }
        val isSupportHidden = savedInstanceState.getBoolean(BaseFragment.ARG_IS_HIDDEN)
        val ft = activity.fragmentManager.beginTransaction()
        if (isSupportHidden) {
            ft.hide(fragment)
        } else {
            ft.show(fragment)
        }
        ft.commit()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putBoolean(ARG_IS_HIDDEN, isHidden)
        super.onSaveInstanceState(outState)

    }

    companion object {
        val ARG_IS_HIDDEN = "ARG_IS_HIDDEN"
    }

}