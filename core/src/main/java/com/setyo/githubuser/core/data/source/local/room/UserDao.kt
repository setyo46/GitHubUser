package com.setyo.githubuser.core.data.source.local.room

import androidx.room.*
import com.setyo.githubuser.core.data.source.local.entity.GitHubUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAllFavoriteUser(): Flow<List<GitHubUserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM users WHERE username = :username)")
    fun getFavoriteUserByUsername(username: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gitHubUserEntity: GitHubUserEntity)

    @Delete
    suspend fun delete(gitHubUserEntity: GitHubUserEntity)

}