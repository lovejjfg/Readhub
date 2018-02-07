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

package com.lovejjfg.readhub.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import io.reactivex.disposables.Disposable


/**
 * Created by Joe on 2016/11/13.
 * Email lovejjfg@gmail.com
 */

interface IBaseView {
    fun showToast(toast: String)

    fun showToast(@StringRes stringId: Int)

    //显示dialog
    fun showLoadingDialog(msg: String)

    //显示dialog
    fun showLoadingDialog(cancelable: Boolean)

    fun closeLoadingDialog()

    fun subscribe(subscriber: Disposable)

    fun unSubscribe()

    fun handleError(throwable: Throwable)

    fun beforeCreate(savedInstanceState: Bundle?)

    fun afterCreatedView(savedInstanceState: Bundle?)

    fun getMyContext() : Context?

}
