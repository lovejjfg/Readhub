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

import com.lovejjfg.readhub.base.AppDelegate
import com.lovejjfg.readhub.data.Constants.API_RELEASE
import com.lovejjfg.readhub.data.Constants.API_SEARCH_RELEASE
import com.lovejjfg.readhub.data.search.SearchResult
import com.lovejjfg.readhub.data.topic.HotTopic
import com.lovejjfg.readhub.data.topic.InstantView
import com.lovejjfg.readhub.data.topic.NewCount
import com.lovejjfg.readhub.data.topic.detail.TopicDetail
import com.lovejjfg.readhub.data.topic.develop.Develop
import com.lovejjfg.readhub.data.topic.tech.Tech
import com.lovejjfg.readhub.utils.convert
import com.lovejjfg.readhub.utils.convertCache
import com.lovejjfg.readhub.utils.http.CacheControlInterceptor
import com.lovejjfg.readhub.utils.http.LoggingInterceptor
import com.lovejjfg.readhub.utils.http.RequestUtils
import io.reactivex.Observable
import okhttp3.Cache
import okhttp3.OkHttpClient.Builder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.SECONDS

/**
 * ReadHub
 * Created by Joe at 2017/7/26.
 */
object DataManager {
    private var retrofit: Retrofit? = null
    private var searchRetrofit: Retrofit? = null
    private const val TIME_OUT = 10L

    private val readHubService: ReadHubService  by lazy {
        init()
    }

    private val readHubSearchService: SearchService  by lazy {
        initSearch()
    }

    private fun <T> init(clazz: Class<T>): T {
        val retrofit = this.retrofit ?: initRetrofit()
        return retrofit.create(clazz)
    }

    private fun initRetrofit(): Retrofit {
        val cacheSize = 10 * 1024 * 1024L
        val cache = Cache(
            AppDelegate.cacheDirectory,
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

    private fun init(): ReadHubService {
        return init(ReadHubService::class.java)
    }

    private fun initSearch(): SearchService {
        val searchRetrofit = this.searchRetrofit ?: initSearchRetrofit()
        return searchRetrofit.create(SearchService::class.java)
    }

    private fun initSearchRetrofit(): Retrofit {
        val cacheSize = 10 * 1024 * 1024L
        val cache = Cache(AppDelegate.cacheDirectory, cacheSize)
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

    fun hotTopic(): Observable<HotTopic> {
        return readHubService.hotTopic().convertCache()
    }

    fun topicInstant(id: String): Observable<InstantView> {
        return readHubService.topicInstant(id).convert()
    }

    fun topicDetail(id: String): Observable<TopicDetail> {
        return readHubService.topicDetail(id).convert()
    }

    fun hotTopicMore(lastId: String, size: Int = 10): Observable<HotTopic> {
        return readHubService.hotTopicMore(lastId, size).convertCache()
    }

    fun tech(): Observable<Tech> {
        return readHubService.tech().convertCache()
    }

    fun techMore(lastId: String, size: Int = 10): Observable<Tech> {
        return readHubService.techMore(lastId, size).convertCache()
    }

    fun devNews(): Observable<Develop> {
        return readHubService.devNews().convertCache()
    }

    fun devNewsMore(lastId: String, size: Int = 10): Observable<Develop> {
        return readHubService.devNewsMore(lastId, size).convertCache()
    }

    fun newCount(lastId: String): Observable<NewCount> {
        return readHubService.newCount(lastId).convert()
    }

    fun blockchain(size: Int = 10): Observable<Develop> {
        return readHubService.blockchain(size).convertCache()
    }

    fun blockchainMore(lastId: String, size: Int = 10): Observable<Develop> {
        return readHubService.blockchainMore(lastId, size).convertCache()
    }

    fun search(map: Map<String, String>): Observable<SearchResult> {
        return readHubSearchService.search(map).convert()
    }
}
