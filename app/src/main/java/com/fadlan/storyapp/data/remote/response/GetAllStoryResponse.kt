package com.fadlan.storyapp.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class GetAllStoryResponse(

	@field:SerializedName("listStory")
	val listStoryItem: List<ListStoryItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

@Entity
data class ListStoryItem(
	val createdAt: String,
	val description: String,
	@PrimaryKey
	val id: String,
	val name: String,
	val photoUrl: String,
	val lat: Double,
	val lon: Double
)