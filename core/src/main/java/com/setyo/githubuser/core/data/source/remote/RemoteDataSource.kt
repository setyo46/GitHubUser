package com.setyo.githubuser.core.data.source.remote

import android.util.Log
import com.setyo.githubuser.core.data.source.remote.network.ApiResponse
import com.setyo.githubuser.core.data.source.remote.network.ApiService
import com.setyo.githubuser.core.data.source.remote.response.DetailUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

class RemoteDataSource (
    private val apiService: ApiService
) {

    suspend fun getListUser(username: String): Flow<ApiResponse<List<DetailUserResponse>>> {
        return flow {
            try {
                val response = apiService.getListUser(username)
                val dataArray = response.items
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.items))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDetailUser(username: String): Flow<ApiResponse<DetailUserResponse>> {
        return flow {
            try {
                val response = apiService.getDetailUser(username)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getFollowerUser(username: String): Flow<ApiResponse<List<DetailUserResponse>>> {
        return flow {
            try {
                val response = apiService.getFollowers(username)
                val dataArray = arrayOf(response)
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getFollowingUser(username: String): Flow<ApiResponse<List<DetailUserResponse>>> {
        return flow {
            try {
                val response = apiService.getFollowing(username)
                val dataArray = arrayOf(response)
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

}