package com.couple.diary.helper.member

import com.couple.diary.application.auth.dto.TokenResponse
import com.couple.diary.application.member.dto.MemberJoinParam
import com.couple.diary.application.member.dto.MemberLoginParam

class UnitTestHelper {
    companion object {
        fun createMemberJoinParam(
            email: String,
            password: String,
            name: String,
            phone: String,
            birth: String,
            gender: String,
            profileImage: String?
        ) = MemberJoinParam(
            email, password, name, phone, birth, gender, profileImage
        )

        fun createMemberLoginParam(
            email: String,
            password: String
        ) = MemberLoginParam(
            email, password
        )

        fun createTokenResponse(
            accessToken: String,
            refreshToken: String
        ) = TokenResponse(
            accessToken, refreshToken
        )
    }
}