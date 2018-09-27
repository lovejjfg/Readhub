package com.lovejjfg.readhub.data.topic.detail

import com.google.gson.annotations.SerializedName
import com.lovejjfg.readhub.data.topic.EntityRelatedTopicsItem
import com.lovejjfg.readhub.data.topic.Extra
import com.lovejjfg.readhub.data.topic.NelData
import com.lovejjfg.readhub.data.topic.NewsArrayItem
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class TopicDetail(

        @field:SerializedName("summary")
        val summary: String? = null,

        @field:SerializedName("nelData")
        val nelData: NelData? = null,

        @field:SerializedName("publishDate")
        val publishDate: String? = null,

        @field:SerializedName("newsArray")
        val newsArray: List<NewsArrayItem?>? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("entityRelatedTopics")
        val entityRelatedTopics: List<EntityRelatedTopicsItem?>? = null,

        @field:SerializedName("createdAt")
        val createdAt: String? = null,

        @field:SerializedName("entityTopics")
        val entityTopics: List<EntityTopicsItem?>? = null,

        @field:SerializedName("extra")
        val extra: Extra? = null,

        @field:SerializedName("timeline")
        val timeline: Timeline? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("entityEventTopics")
        val entityEventTopics: List<Any?>? = null,

        @field:SerializedName("order")
        val order: String? = null,

        @field:SerializedName("updatedAt")
        val updatedAt: String? = null
)
