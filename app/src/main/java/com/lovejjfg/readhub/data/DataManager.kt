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

package com.lovejjfg.readhub.data

import com.lovejjfg.readhub.data.Constants.API_RELEASE
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * ReadHub
 * Created by Joe at 2017/7/26.
 */
object DataManager {


    private var retrofit: Retrofit? = null
    private val isDebug = true

    fun <T> init(clazz: Class<T>): T {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(API_RELEASE)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit!!.create(clazz)
    }

    fun init(): ReadHubService {
        return init(ReadHubService::class.java)
    }

    fun <R> convert(request: Observable<Response<R>>): Observable<R> {

        return request
                .subscribeOn(Schedulers.io())//事件产生在子线程
                .retry({ integer, throwable ->
                    (throwable is UnknownHostException || throwable is SocketTimeoutException) && integer <= 2
                })
                .map { t: Response<R> ->
                    t.body()!!
                }
    }

    fun <R> subscribe(request: Observable<Response<R>>, onNext: Consumer<R>, onError: Consumer<Throwable>): Disposable {
        return convert(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError)

    }

    fun <R> mapScribe(request: Observable<Response<R>>, onNext: Consumer<R>, onError: Consumer<Throwable>): Disposable {
        return convert(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError)

    }


}