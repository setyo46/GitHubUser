package com.setyo.githubuser.core.data.source.local

import com.setyo.githubuser.core.data.source.local.entity.GitHubUserEntity
import com.setyo.githubuser.core.data.source.local.room.UserDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val mUserDao : UserDao
) {

    fun getAllFavoriteUser(): Flow<List<GitHubUserEntity>> = mUserDao.getAllFavoriteUser()

    suspend fun insertUser(user: GitHubUserEntity) = mUserDao.insert(user)

    suspend fun deleteUser(user: GitHubUserEntity) = mUserDao.delete(user)

    fun getFavoriteUserByUsername(username: String): Flow<Boolean> = mUserDao.getFavoriteUserByUsername(username)

}