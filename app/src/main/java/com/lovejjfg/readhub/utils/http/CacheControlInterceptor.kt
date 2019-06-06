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

package com.lovejjfg.readhub.utils.http

import android.util.Log
import com.lovejjfg.readhub.base.AppProxy
import com.lovejjfg.readhub.utils.RxBus
import com.lovejjfg.readhub.utils.event.NoNetEvent
import com.lovejjfg.readhub.utils.isNetworkConnected
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by Joe on 2017/1/4.
 * Email lovejjfg@gmail.com
 */

class CacheControlInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        return if (!AppProxy.applicationContext.isNetworkConnected()) {
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
            RxBus.instance.post(NoNetEvent())
            Log.i(TAG, "intercept: 没有网络。。")
            val cacheControl = request.cacheControl().toString()
            chain.proceed(request)
                .newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", cacheControl)
                .build()
        } else {
            chain.proceed(request)
        }
    }

    companion object {
        private val TAG = CacheControlInterceptor::class.java.simpleName
    }
}
