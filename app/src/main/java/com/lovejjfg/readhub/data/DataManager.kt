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

import com.lovejjfg.readhub.base.AppProxy
import com.lovejjfg.readhub.base.IBaseView
import com.lovejjfg.readhub.base.ReadhubException
import com.lovejjfg.readhub.data.Constants.API_RELEASE
import com.lovejjfg.readhub.data.Constants.API_SEARCH_RELEASE
import com.lovejjfg.readhub.utils.http.CacheControlInterceptor
import com.lovejjfg.readhub.utils.http.LoggingInterceptor
import com.lovejjfg.readhub.utils.http.RequestUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient.Builder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit.SECONDS

/**
 * ReadHub
 * Created by Joe at 2017/7/26.
 */
object DataManager {
    //https://search.readhub.cn/api/entity/news?page=1&size=20&ner_name=%E9%A9%AC%E6%96%AF%E5%85%8B&entity_id=baike_3776526&type=hot
    private var retrofit: Retrofit? = null
    private var searchRetrofit: Retrofit? = null
    private val isDebug = false
    private const val TIME_OUT = 10L

    fun <T> init(clazz: Class<T>): T {
        val retrofit = this.retrofit ?: initRetrofit()
        return retrofit.create(clazz)
    }

    private fun initRetrofit(): Retrofit {
        val cacheSize = 10 * 1024 * 1024L
        val cache = Cache(
            AppProxy.cacheDirectory ?: throw NullPointerException("init cacheDirectory error"),
            cacheSize
        )
        val retrofit = Retrofit.Builder()
            .baseUrl(API_RELEASE)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(Builder()
                .cache(cache)
                .connectTimeout(TIME_OUT, SECONDS)
                .readTimeout(TIME_OUT, SECONDS)
                .writeTimeout(TIME_OUT, SECONDS)
                .addInterceptor { chain -> chain.proceed(RequestUtils.createNormalHeader(chain.request())) }
                .addInterceptor(CacheControlInterceptor())
                .addInterceptor(LoggingInterceptor())
                .build()
            )
            .build()
        this.retrofit = retrofit
        return retrofit
    }

    fun init(): ReadHubService {
        return init(ReadHubService::class.java)
    }

    fun initSearch(): SearchService {
        val searchRetrofit = this.searchRetrofit ?: initSearchRetrofit()
        return searchRetrofit.create(SearchService::class.java)
    }

    private fun initSearchRetrofit(): Retrofit {
        val cacheSize = 10 * 1024 * 1024L
        val cache = Cache(AppProxy.cacheDirectory ?: throw NullPointerException("init cacheDirectory error"), cacheSize)
        val searchRetrofit = Retrofit.Builder()
            .baseUrl(API_SEARCH_RELEASE)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(Builder()
                .cache(cache)
                .connectTimeout(TIME_OUT, SECONDS)
                .readTimeout(TIME_OUT, SECONDS)
                .writeTimeout(TIME_OUT, SECONDS)
                .addInterceptor { chain -> chain.proceed(RequestUtils.createNormalHeader(chain.request())) }
                .addInterceptor(CacheControlInterceptor())
                .addInterceptor(LoggingInterceptor())
                .build()
            )
            .build()
        this.searchRetrofit = searchRetrofit
        return searchRetrofit
    }

    fun <R> convert(request: Observable<Response<R>>): Observable<R> {

        return request
            .subscribeOn(Schedulers.io())//事件产生在子线程
            .retry { integer, throwable ->
                (throwable is UnknownHostException || throwable is SocketTimeoutException) && integer <= 2
            }
            .map { t: Response<R> ->
                return@map if (t.isSuccessful) {
                    t.body() ?: throw NullPointerException("null Body")
                } else {
                    throw ReadhubException(t.code(), t.message())
                }
            }
    }

    fun <R> subscribe(
        view: IBaseView,
        request: Observable<Response<R>>,
        onNext: Consumer<R>,
        onError: Consumer<Throwable>
    ) {
        val subscribe = convert(request)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext, onError)
        view.subscribe(subscribe)
    }

    fun <R> mapScribe(request: Observable<Response<R>>, onNext: Consumer<R>, onError: Consumer<Throwable>): Disposable {
        return convert(request)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext, onError)
    }
}
