package com.setyo.githubuser.di

import com.setyo.githubuser.core.domain.usecase.GithubUserUseCase
import com.setyo.githubuser.core.domain.usecase.GithubUsersInteractor
import com.setyo.githubuser.presentation.detail.DetailViewModel
import com.setyo.githubuser.presentation.home.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<GithubUserUseCase> { GithubUsersInteractor(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}