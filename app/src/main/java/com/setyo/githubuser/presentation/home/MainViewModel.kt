package com.setyo.githubuser.presentation.home

import androidx.lifecycle.*
import com.setyo.githubuser.core.domain.usecase.GithubUserUseCase

class MainViewModel(
    private val githubUserUseCase: GithubUserUseCase,
) : ViewModel() {

    fun getListUser(username: String) = githubUserUseCase.getListUser(username).asLiveData()

}