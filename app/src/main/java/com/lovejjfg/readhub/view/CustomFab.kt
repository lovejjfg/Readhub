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

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.FloatingActionButton.OnVisibilityChangedListener
import android.util.AttributeSet
import com.lovejjfg.readhub.utils.UIUtil

/**
 * ReadHub
 * Created by Joe at 2017/9/5.
 */
class CustomFab : FloatingActionButton {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun show() {

    }

    override fun show(listener: OnVisibilityChangedListener?) {
        super.show(object : OnVisibilityChangedListener() {
            override fun onShown(fab: FloatingActionButton?) {
                super.onShown(fab)
            }

            override fun onHidden(fab: FloatingActionButton?) {
                super.onHidden(fab)
            }
        })
    }


}