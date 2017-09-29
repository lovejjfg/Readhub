package com.lovejjfg.readhub.data.topic.detail

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class Timeline(

        @field:SerializedName("keywords")
        val keywords: List<String?>? = null,

        @field:SerializedName("topics")
        val topics: List<TopicsItem?>? = null,

        @field:SerializedName("errorCode")
        val errorCode: Int? = null,

        @field:SerializedName("message")
        val message: String? = null
)