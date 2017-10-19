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


import com.lovejjfg.readhub.BuildConfig
import okhttp3.Request
import java.util.*

/**
 * Created by Joe on 2016-05-27
 * Email: lovejjfg@163.com
 */
object RequestUtils {

    private val languageEnv: String
        get() {
            return try {
                val l = Locale.getDefault()
                val language = l.language
                val country = l.country
                String.format("%s-%s", language, country)
            } catch (e: Exception) {
                e.printStackTrace()
                "zh-CN"
            }

        }

    val userAgent: String
        get() = String.format("Readhub Android/%s/%s", BuildConfig.VERSION_NAME, "Joe")


    fun createNormalHeader(request: Request): Request {

        return request.newBuilder()
                .addHeader("Accept-Language", languageEnv)
                .addHeader("User-Agent", userAgent)
                .build()
    }


}
