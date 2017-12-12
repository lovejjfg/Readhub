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

package com.lovejjfg.readhub.utils

import android.content.Context
import android.util.Log
import com.lovejjfg.readhub.utils.http.ToastUtil
import com.tencent.bugly.crashreport.CrashReport
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Joe on 2017/1/5.
 * Email lovejjfg@gmail.com
 */

object ErrorUtil {


    fun handleError(context: Context, throwable: Throwable) {
        Log.e("ErrorUtil", "handleError: ", throwable)
        CrashReport.postCatchedException(throwable)
        //        view.showErrorView();
        if (throwable is HttpException) {
            val code = throwable.code()

            if (code == 504) {
                ToastUtil.showToast(context, "请确认你的网络连接")
            }
            return
        }
        if (throwable is SocketTimeoutException) {
            ToastUtil.showToast(context, "网络连接超时!")
            return
        }
        if (throwable is ConnectException || throwable is UnknownHostException) {
            ToastUtil.showToast(context, "网络连接出问题啦!")
        }
    }
}
