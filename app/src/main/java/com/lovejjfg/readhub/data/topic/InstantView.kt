package com.lovejjfg.readhub.data.topic

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
@Parcelize
data class InstantView(

        @field:SerializedName("siteName")
        val siteName: String? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("siteSlug")
        val siteSlug: String? = null,

        @field:SerializedName("url")
        val url: String? = null,

        @field:SerializedName("content")
        val content: String? = null
) : Parcelable