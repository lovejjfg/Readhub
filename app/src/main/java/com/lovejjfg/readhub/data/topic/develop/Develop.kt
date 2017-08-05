package com.lovejjfg.readhub.data.topic.develop

import com.google.gson.annotations.SerializedName
import com.lovejjfg.readhub.data.topic.DataItem
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class Develop(

        @field:SerializedName("totalItems")
        val totalItems: Int? = null,

        @field:SerializedName("data")
        val data: List<DataItem?>? = null,

        @field:SerializedName("totalPages")
        val totalPages: Int? = null,

        @field:SerializedName("pageSize")
        val pageSize: Int? = null
)