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

/**
 * Created by Joe on 2017/3/14.
 * Email lovejjfg@gmail.com
 */

data class Library(var name: String?, var des: String?, var jumpUrl: String?) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(des)
        writeString(jumpUrl)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Library> = object : Parcelable.Creator<Library> {
            override fun createFromParcel(source: Parcel): Library = Library(source)
            override fun newArray(size: Int): Array<Library?> = arrayOfNulls(size)
        }
    }
}
