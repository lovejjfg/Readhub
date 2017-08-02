package com.lovejjfg.readhub

import com.lovejjfg.readhub.data.topic.HotTopic
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

}