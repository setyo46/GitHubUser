package com.setyo.githubuser.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GithubResponse(

	@field:SerializedName("total_count")
	val totalCount: Int?,

	@field:SerializedName("items")
	val items: List<DetailUserResponse>
)