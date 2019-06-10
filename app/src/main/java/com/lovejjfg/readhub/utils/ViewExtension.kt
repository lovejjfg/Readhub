/*
 * Copyright (c) 2019.  Joe
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

package com.lovejjfg.readhub.utils

import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by joe on 2019-05-09.
 * Email: lovejjfg@gmail.com
 */
inline fun ViewGroup.inflate(@LayoutRes resId: Int, attachParent: Boolean = false): View {
    return LayoutInflater.from(this.context).inflate(resId, this, attachParent)
}

inline fun View.canScrollUp(): Boolean {
    return this.canScrollVertically(-1)
}

inline fun View.canScrollDown(): Boolean {
    return this.canScrollVertically(1)
}


