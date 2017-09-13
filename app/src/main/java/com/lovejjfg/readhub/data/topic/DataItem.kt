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

package com.lovejjfg.readhub.data.topic

import android.os.Parcel
import android.os.Parcelable
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
        val order: String? = null,

        @field:SerializedName("updatedAt")
        val updatedAt: String? = null,

        @field:SerializedName("authorName")
        val authorName: String? = null,

        @field:SerializedName("siteName")
        val siteName: String? = null,

        @field:SerializedName("url")
        val url: String? = null,

        @field:SerializedName("extra")
        val extra: Extra? = null


) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<DataItem> = object : Parcelable.Creator<DataItem> {
            override fun createFromParcel(source: Parcel): DataItem = DataItem(source)
            override fun newArray(size: Int): Array<DataItem?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.readString(),
            ArrayList<Any?>().apply { source.readList(this, Any::class.java.classLoader) },
            source.readParcelable<NelData>(NelData::class.java.classLoader),
            source.readString(),
            source.readString(),
            ArrayList<NewsArrayItem?>().apply { source.readList(this, NewsArrayItem::class.java.classLoader) },
            source.readString(),
            source.createTypedArrayList(EntityRelatedTopicsItem.CREATOR),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(isExband)
        dest.writeString(summary)
        dest.writeString(createdAt)
        dest.writeList(relatedTopicArray)
        dest.writeParcelable(nelData, 0)
        dest.writeString(publishDate)
        dest.writeString(id)
        dest.writeList(newsArray)
        dest.writeString(title)
        dest.writeTypedList(entityRelatedTopics)
        dest.writeString(order)
        dest.writeString(updatedAt)
        dest.writeString(authorName)
        dest.writeString(siteName)
        dest.writeString(url)
    }
}