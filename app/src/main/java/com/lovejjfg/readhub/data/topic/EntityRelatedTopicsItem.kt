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
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class EntityRelatedTopicsItem(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("entityName")
	val entityName: String? = null,

	@field:SerializedName("entityId")
	val entityId: Int? = null,

	@field:SerializedName("eventTypeLabel")
	val eventTypeLabel: String? = null,

	@field:SerializedName("eventType")
	val eventType: Int? = null
) : Parcelable {
	companion object {
		@JvmField val CREATOR: Parcelable.Creator<EntityRelatedTopicsItem> = object : Parcelable.Creator<EntityRelatedTopicsItem> {
			override fun createFromParcel(source: Parcel): EntityRelatedTopicsItem = EntityRelatedTopicsItem(source)
			override fun newArray(size: Int): Array<EntityRelatedTopicsItem?> = arrayOfNulls(size)
		}
	}

	constructor(source: Parcel) : this(
	source.createTypedArrayList(DataItem.CREATOR),
	source.readString(),
	source.readValue(Int::class.java.classLoader) as Int?,
	source.readString(),
	source.readValue(Int::class.java.classLoader) as Int?
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeTypedList(data)
		dest.writeString(entityName)
		dest.writeValue(entityId)
		dest.writeString(eventTypeLabel)
		dest.writeValue(eventType)
	}
}