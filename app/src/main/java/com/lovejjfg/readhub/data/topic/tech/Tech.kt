package com.lovejjfg.readhub.data.topic.tech

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName
import com.lovejjfg.readhub.data.topic.DataItem

@Generated("com.robohorse.robopojogenerator")
data class Tech(

	@field:SerializedName("totalItems")
	val totalItems: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("totalPages")
	val totalPages: Int? = null,

	@field:SerializedName("pageSize")
	val pageSize: Int? = null
)