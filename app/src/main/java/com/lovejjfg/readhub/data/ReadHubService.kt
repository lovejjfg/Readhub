package com.lovejjfg.readhub.data

import com.lovejjfg.readhub.data.topic.HotTopic
import com.lovejjfg.readhub.data.topic.develop.Develop
import com.lovejjfg.readhub.data.topic.tech.Tech
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * ReadHub
 * Created by Joe at 2017/7/27.
 */
interface ReadHubService {

    @GET("topic")
    fun hotTopic(): Observable<Response<HotTopic>>

    @GET("topic")
    fun hotTopicMore(@Query("lastCursor") lastId: String, @Query("pageSize") size: Int): Observable<Response<HotTopic>>

    @GET("news")
    fun tech(): Observable<Response<Tech>>

    @GET("news")
    fun techMore(@Query("lastCursor") lastId: String, @Query("pageSize") size: Int): Observable<Response<Tech>>

    @GET("technews")
    fun devNews(): Observable<Response<Develop>>

    @GET("technews")
    fun devNewsMore(@Query("lastCursor") lastId: String, @Query("pageSize") size: Int): Observable<Response<Develop>>

}