package com.lovejjfg.readhub.data.topic

import android.os.Parcel
import android.os.Parcelable
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class NelData(

	@field:SerializedName("result")
	val result: List<ResultItem?>? = null
) : Parcelable {
	companion object {
		@JvmField val CREATOR: Parcelable.Creator<NelData> = object : Parcelable.Creator<NelData> {
			override fun createFromParcel(source: Parcel): NelData = NelData(source)
			override fun newArray(size: Int): Array<NelData?> = arrayOfNulls(size)
		}
	}

	constructor(source: Parcel) : this(
	ArrayList<ResultItem?>().apply { source.readList(this, ResultItem::class.java.classLoader) }
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeList(result)
	}
}