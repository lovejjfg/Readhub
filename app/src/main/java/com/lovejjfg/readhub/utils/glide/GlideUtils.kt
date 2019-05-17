/*
 * Copyright (c) 2017.  Joe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lovejjfg.readhub.utils.glide

import android.widget.ImageView

/**
 * Created by joe on 2017/9/29.
 * Email: lovejjfg@gmail.com
 */
object GlideUtils {
    fun into(url: String?, iv: ImageView?) {
        try {
            if (url == null || iv == null) {
                return
            }
            GlideApp.with(iv.context)
                .load(url)
                .into(iv)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
