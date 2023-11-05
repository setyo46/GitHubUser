package com.setyo.githubuser.favorite.di

import com.setyo.githubuser.favorite.presentation.FavoriteUserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteModule = module {
    viewModel { FavoriteUserViewModel(get()) }
}