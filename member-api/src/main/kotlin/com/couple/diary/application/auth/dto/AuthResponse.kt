package com.couple.diary.application.auth.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)