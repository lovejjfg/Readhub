package com.lovejjfg.readhub.data.topic

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class DataItem(
        @field:SerializedName("isExband")
        var isExband: Boolean? = false,

        @field:SerializedName("summary")
        val summary: String? = null,

        @field:SerializedName("createdAt")
        val createdAt: String? = null,

        @field:SerializedName("relatedTopicArray")
        val relatedTopicArray: List<Any?>? = null,

        @field:SerializedName("nelData")
        val nelData: NelData? = null,

        @field:SerializedName("publishDate")
        val publishDate: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("newsArray")
        val newsArray: List<NewsArrayItem?>? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("entityRelatedTopics")
        val entityRelatedTopics: List<EntityRelatedTopicsItem?>? = null,

        @field:SerializedName("order")
        val order: Int? = null,

        @field:SerializedName("updatedAt")
        val updatedAt: String? = null
)