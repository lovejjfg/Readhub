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

import com.lovejjfg.readhub.data.topic.HotTopic
import com.lovejjfg.readhub.data.topic.InstantView
import com.lovejjfg.readhub.data.topic.NewCount
import com.lovejjfg.readhub.data.topic.detail.TopicDetail
import com.lovejjfg.readhub.data.topic.develop.Develop
import com.lovejjfg.readhub.data.topic.tech.Tech
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * ReadHub
 * Created by Joe at 2017/7/27.
 */
interface ReadHubService {

    @GET("topic")
    fun hotTopic(): Observable<Response<HotTopic>>

    //https://api.readhub.me/topic/instantview?topicId=RwOK8HoWNG
    @GET("topic/instantview")
    fun topicInstant(@Query("topicId") id: String): Observable<Response<InstantView>>

    @GET("topic/{id}")
    fun topicDetail(@Path("id") id: String): Observable<Response<TopicDetail>>

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

    @GET("topic/newCount")
    fun newCount(@Query("latestCursor") lastId: String): Observable<Response<NewCount>>

    //https://api.readhub.me/blockchain?lastCursor=1520307600000&pageSize=10
    @GET("blockchain")
    fun blockchain(@Query("lastCursor") lastId: String, @Query("pageSize") size: Int): Observable<Response<Develop>>


}