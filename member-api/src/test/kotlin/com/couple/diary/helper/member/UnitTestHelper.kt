package com.couple.diary.helper.member

import com.couple.diary.application.member.dto.MemberJoinParam

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
    }
}