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

package com.lovejjfg.readhub.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Test(

        @field:SerializedName("name")
        val name: String? = "xxxxx",

        @field:SerializedName("age")
        val age: Int? = null
) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Test> = object : Parcelable.Creator<Test> {
            override fun createFromParcel(source: Parcel): Test = Test(source)
            override fun newArray(size: Int): Array<Test?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeValue(age)
    }
}