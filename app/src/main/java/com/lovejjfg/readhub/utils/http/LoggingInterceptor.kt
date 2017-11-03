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
import com.lovejjfg.readhub.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import okio.BufferedSource
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException

/**
 * Created by Joe on 2017/1/4.
 * Email lovejjfg@gmail.com
 */

class LoggingInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (!BuildConfig.IS_DEBUG) {
            return chain.proceed(request)
        }
        val t1 = System.nanoTime()
        Log.i(TAG, String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()))
        try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            val body = copy.body()
            if (body != null) {
                body.writeTo(buffer)
                val s = buffer.readUtf8()
            }
            val response = chain.proceed(request)
            if (response.isSuccessful) {
                logResponse(t1, response)
            } else {
                logResponse(t1, response)
                Log.e(TAG, "code:" + response.code() + " msg:" + response.message())
            }
            return response
        } catch (e: IOException) {
            Log.e(TAG, "intercept: ", e)
            return chain.proceed(request)
        }
    }

    @Throws(IOException::class)
    private fun logResponse(t1: Long, response: Response) {
        val responseBody = response.body()
        val source: BufferedSource
        if (responseBody != null) {
            source = responseBody.source()
        } else {
            return
        }
        val t2 = System.nanoTime()
        val format = String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6, response.headers())
        Log.i(TAG, format)
        source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source.buffer()

        var charset: Charset? = UTF8
        val contentType = responseBody.contentType()
        val contentLength = responseBody.contentLength()
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8)
            } catch (e: UnsupportedCharsetException) {
                Log.i(TAG, "intercept: " + "Couldn't decode the response body; charset is likely malformed.")
                Log.i(TAG, "intercept: " + "<-- END HTTP")
            }

        }

        if (contentLength != 0L) {
            var json: String? = null
            try {
                json = buffer.clone().readString(charset!!)
                Log.i(TAG, json)
            } catch (e: Exception) {
                Log.e(TAG, json)
            }

        }

        Log.i(TAG, "intercept: " + "<-- END HTTP (" + buffer.size() + "-byte body)")
    }

    companion object {
        private val UTF8 = Charset.forName("UTF-8")
        private val TAG = LoggingInterceptor::class.java.simpleName
    }
}
