package com.couple.diary.application.member.dto

data class MemberJoinParam(
    val email: String,
    val password: String,
    val name: String,
    val phone: String,
    val birth: String,
    val gender: String,
    val profileImage: String?
)

data class MemberLoginParam(
    val email: String,
    val password: String
)