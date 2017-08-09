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