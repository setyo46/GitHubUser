package com.setyo.githubuser.core.domain.usecase

import com.setyo.githubuser.core.data.Resource
import com.setyo.githubuser.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface GithubUserUseCase {

    fun getListUser(username: String): Flow<Resource<List<User>>>

    fun getDetailUser(username: String): Flow<Resource<User>>

    fun getFollowersUser(username: String): Flow<Resource<List<User>>>

    fun getFollowingUser(username: String): Flow<Resource<List<User>>>

    fun getAllFavoriteUser(): Flow<List<User>>

    suspend fun insertUser(users: User)

    suspend fun deleteUser(users: User)

    fun getFavoriteUserByUsername(username: String): Flow<Boolean>
}