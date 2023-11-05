package com.setyo.githubuser.core.domain.model

data class User(
    val login: String,
    val name: String?,
    val avatarUrl: String?,
    val followers: Int?,
    val following: Int?,

)
