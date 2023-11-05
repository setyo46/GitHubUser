package com.setyo.githubuser.core.utils

import com.setyo.githubuser.core.data.source.local.entity.GitHubUserEntity
import com.setyo.githubuser.core.data.source.remote.response.DetailUserResponse
import com.setyo.githubuser.core.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object DataMapper {

    fun mapDomainToEntity(input: User) = GitHubUserEntity(
        login = input.login,
        name = input.name,
        avatarUrl = input.avatarUrl,
        followers = input.followers,
        following = input.following,
    )

    fun mapResponseToDomain(input: List<DetailUserResponse>): Flow<List<User>> {
        val userList = ArrayList<User>()
        input.map {
            val user = User(
                login = it.login,
                name = it.name,
                avatarUrl = it.avatarUrl,
                followers = it.followers,
                following = it.following
            )
            userList.add(user)
        }
        return flowOf(userList)
    }

    fun mapResponsesToDomain(input: DetailUserResponse): Flow<User> {
        return flowOf(
            User(
                login = input.login,
                name = input.name,
                avatarUrl = input.avatarUrl,
                followers = input.followers,
                following = input.following,
            )
        )
    }


    fun mapEntitiesToDomain(input: List<GitHubUserEntity>): List<User> =
    input.map {
        User(
            login = it.login,
            name = it.name,
            avatarUrl = it.avatarUrl,
            followers = it.followers,
            following = it.following
        )
    }

}