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

import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.view.WebActivity

/**
 * ReadHub
 * Created by Joe at 2017/8/5.
 */

object JumpUitl {

    fun jumpWeb(context: Context, url: String) {
        val intent = Intent(context, WebActivity::class.java)
        intent.putExtra(Constants.URL, url)
        context.startActivity(intent)
    }
}
