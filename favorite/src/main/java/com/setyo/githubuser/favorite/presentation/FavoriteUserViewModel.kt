package com.setyo.githubuser.favorite.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.setyo.githubuser.core.domain.usecase.GithubUserUseCase

class FavoriteUserViewModel(
    private val useCase: GithubUserUseCase
): ViewModel() {

    fun getAllFavoriteUser() = useCase.getAllFavoriteUser().asLiveData()
}