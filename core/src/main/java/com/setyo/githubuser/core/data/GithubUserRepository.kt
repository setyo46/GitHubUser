package com.setyo.githubuser.core.data

import com.setyo.githubuser.core.data.source.local.LocalDataSource
import com.setyo.githubuser.core.data.source.remote.RemoteDataSource
import com.setyo.githubuser.core.data.source.remote.network.ApiResponse
import com.setyo.githubuser.core.data.source.remote.response.DetailUserResponse
import com.setyo.githubuser.core.domain.model.User
import com.setyo.githubuser.core.domain.repository.IGithubUserRepository
import com.setyo.githubuser.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GithubUserRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
): IGithubUserRepository {
    override fun getListUser(username: String): Flow<Resource<List<User>>> =
       object : NetworkBoundResource<List<User>, List<DetailUserResponse>>() {
           override fun loadFromNetwork(data: List<DetailUserResponse>): Flow<List<User>> {
               return DataMapper.mapResponseToDomain(data)
           }

           override suspend fun createCall(): Flow<ApiResponse<List<DetailUserResponse>>> =
               remoteDataSource.getListUser(username)

       }.asFlow()


    override fun getDetailUser(username: String): Flow<Resource<User>> =
        object : NetworkBoundResource<User, DetailUserResponse>() {
            override fun loadFromNetwork(data: DetailUserResponse): Flow<User> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<DetailUserResponse>> =
                remoteDataSource.getDetailUser(username)

        }.asFlow()


    override fun getFollowerUser(username: String): Flow<Resource<List<User>>> =
        object : NetworkBoundResource<List<User>, List<DetailUserResponse>>() {
            override fun loadFromNetwork(data: List<DetailUserResponse>): Flow<List<User>> {
                return DataMapper.mapResponseToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<DetailUserResponse>>> =
                remoteDataSource.getFollowerUser(username)

        }.asFlow()

    override fun getFollowingUser(username: String): Flow<Resource<List<User>>> =
        object : NetworkBoundResource<List<User>, List<DetailUserResponse>>() {
            override fun loadFromNetwork(data: List<DetailUserResponse>): Flow<List<User>> {
                return DataMapper.mapResponseToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<DetailUserResponse>>> =
                remoteDataSource.getFollowingUser(username)

        }.asFlow()


    override fun getAllFavoriteUser(): Flow<List<User>> {
        return localDataSource.getAllFavoriteUser().map { userEntities ->
            userEntities.let {
                DataMapper.mapEntitiesToDomain(it)
            }
        }
    }

    override suspend fun insertUser(users: User) =
       localDataSource.insertUser(DataMapper.mapDomainToEntity(users))


    override suspend fun deleteUse(users: User) =
        localDataSource.deleteUser(DataMapper.mapDomainToEntity(users))


    override fun getFavoriteUserByUsername(username: String): Flow<Boolean> =
        localDataSource.getFavoriteUserByUsername(username)

}