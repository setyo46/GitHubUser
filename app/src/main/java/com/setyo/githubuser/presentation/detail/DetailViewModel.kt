package com.setyo.githubuser.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.setyo.githubuser.core.domain.model.User
import com.setyo.githubuser.core.domain.usecase.GithubUserUseCase
import kotlinx.coroutines.launch

class DetailViewModel(private val useCase: GithubUserUseCase): ViewModel() {

    fun getDetailUser(username: String) = useCase.getDetailUser(username).asLiveData()

    fun getFollowersUser(username: String) = useCase.getFollowersUser(username).asLiveData()

    fun getFollowingUser(username: String) = useCase.getFollowingUser(username).asLiveData()

    fun insertUser(user: User) {
        viewModelScope.launch {
            useCase.insertUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            useCase.deleteUser(user)
        }
    }

    fun getFavoriteUserByUsername(username: String) = useCase.getFavoriteUserByUsername(username).asLiveData()


}

