package com.setyo.githubuser.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.setyo.githubuser.core.data.source.local.entity.GitHubUserEntity

@Database(entities = [GitHubUserEntity::class], version = 1)
abstract class UserRoomDatabase : RoomDatabase() {

    abstract fun favoriteUserDao(): UserDao

}