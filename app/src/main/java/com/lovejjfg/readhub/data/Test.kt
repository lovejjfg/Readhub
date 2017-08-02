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