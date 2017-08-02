package com.lovejjfg.readhub.data.topic

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
)