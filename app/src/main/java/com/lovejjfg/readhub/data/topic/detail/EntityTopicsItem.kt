package com.lovejjfg.readhub.data.topic.detail

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class EntityTopicsItem(

        @field:SerializedName("entityName")
        val entityName: String? = null,

        @field:SerializedName("topics")
        val topics: List<TopicsItem?>? = null,

        @field:SerializedName("entityId")
        val entityId: String? = null,

        @field:SerializedName("eventTypeLabel")
        val eventTypeLabel: String? = null,

        @field:SerializedName("eventType")
        val eventType: String? = null
)
