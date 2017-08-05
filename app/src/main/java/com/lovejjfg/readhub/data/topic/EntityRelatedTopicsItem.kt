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