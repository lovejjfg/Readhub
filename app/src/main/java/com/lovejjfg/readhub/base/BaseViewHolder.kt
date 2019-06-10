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

package com.lovejjfg.readhub.base

import android.view.View
import com.lovejjfg.powerrecyclerx.PowerHolder
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by joe on 2019-05-09.
 * Email: lovejjfg@gmail.com
 */
abstract class BaseViewHolder<Item>(override val containerView: View, enablClick: Boolean = true) : PowerHolder<Item>
    (containerView, enablClick),
    LayoutContainer
