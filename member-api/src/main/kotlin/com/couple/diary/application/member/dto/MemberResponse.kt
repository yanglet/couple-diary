package com.couple.diary.application.member.dto

data class MemberReadResponse(
    val memberNo: Long,
    val email: String,
    val name: String,
    val phone: String,
    val birth: String,
    val gender: String,
    val coupleNo: Long?,
    val profileImage: String?
)

data class MemberLoginResponse(
    val accessToken: String,
    val refreshToken: String
)