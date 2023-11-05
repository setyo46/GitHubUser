package com.setyo.githubuser.core.domain.usecase

import com.setyo.githubuser.core.data.Resource
import com.setyo.githubuser.core.domain.model.User
import com.setyo.githubuser.core.domain.repository.IGithubUserRepository
import kotlinx.coroutines.flow.Flow

class GithubUsersInteractor(
    private val userRepository: IGithubUserRepository,
) : GithubUserUseCase {
    override fun getListUser(username: String): Flow<Resource<List<User>>> =
        userRepository.getListUser(username)

    override fun getDetailUser(username: String): Flow<Resource<User>> =
        userRepository.getDetailUser(username)

    override fun getFollowersUser(username: String): Flow<Resource<List<User>>> =
        userRepository.getFollowerUser(username)

    override fun getFollowingUser(username: String): Flow<Resource<List<User>>> =
        userRepository.getFollowingUser(username)

    override fun getAllFavoriteUser(): Flow<List<User>> =
        userRepository.getAllFavoriteUser()

    override suspend fun insertUser(users: User) =
        userRepository.insertUser(users)

    override suspend fun deleteUser(users: User) =
        userRepository.deleteUse(users)

    override fun getFavoriteUserByUsername(username: String): Flow<Boolean> =
        userRepository.getFavoriteUserByUsername(username)


}