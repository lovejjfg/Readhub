package com.lovejjfg.readhub.data.topic.detail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.lovejjfg.readhub.data.topic.SourcesItem
import kotlinx.android.parcel.Parcelize
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class TopicsItem(

        @field:SerializedName("createdAt")
        val createdAt: String? = null,

        @field:SerializedName("sources")
        val sources: List<SourcesItem?>? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("mobileUrl")
        val mobileUrl: String? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("url")
        val url: String? = null
)