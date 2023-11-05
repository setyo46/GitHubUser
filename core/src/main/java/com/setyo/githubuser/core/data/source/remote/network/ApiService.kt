package com.setyo.githubuser.core.data.source.remote.network

import com.setyo.githubuser.core.data.source.remote.response.DetailUserResponse
import com.setyo.githubuser.core.data.source.remote.response.GithubResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun getListUser(@Query("q") username: String
    ): GithubResponse

    @GET("users/{username}")
    suspend fun getDetailUser(@Path("username") username: String
    ): DetailUserResponse

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String
    ): List<DetailUserResponse>

    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String
    ): List<DetailUserResponse>

}