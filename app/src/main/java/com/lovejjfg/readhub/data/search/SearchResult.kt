package com.lovejjfg.readhub.data.search

import com.google.gson.annotations.SerializedName
import com.lovejjfg.readhub.data.Cache

data class SearchResult(

    @field:SerializedName("data")
    val data: Data? = null,
    override var fromCache: Boolean = false
) : Cache

data class Data(

    @field:SerializedName("totalItems")
    val totalItems: Int,

    @field:SerializedName("startIndex")
    val startIndex: Int,

    @field:SerializedName("pageIndex")
    val pageIndex: Int,

    @field:SerializedName("itemsPerPage")
    val itemsPerPage: Int,

    @field:SerializedName("currentItemCount")
    val currentItemCount: Int,

    @field:SerializedName("totalPages")
    val totalPages: Int,

    @field:SerializedName("items")
    val items: List<SearchItem>
)

data class SearchItem(

    @field:SerializedName("newsList")
    val newsList: List<NewsListItem>? = null,

    @field:SerializedName("topicId")
    val topicId: String,

    @field:SerializedName("hasTimeline")
    val hasTimeline: Boolean? = null,

    @field:SerializedName("topicSummary")
    val topicSummary: String? = null,

    @field:SerializedName("inContentOnly")
    val inContentOnly: Boolean? = null,

    @field:SerializedName("topicCreateAt")
    val topicCreateAt: String? = null,

    @field:SerializedName("key")
    val key: String? = null,

    @field:SerializedName("topicState")
    val topicState: String? = null,

    @field:SerializedName("topicTitle")
    val topicTitle: String? = null,

    var isExpand: Boolean = false
)

data class NewsListItem(

    @field:SerializedName("summary")
    val summary: String? = null,

    @field:SerializedName("hidden")
    val hidden: Boolean? = null,

    @field:SerializedName("publishDate")
    val publishDate: String? = null,

    @field:SerializedName("siteName")
    val siteName: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("url")
    val url: String? = null,

    @field:SerializedName("isShow")
    val isShow: Boolean? = null
)
