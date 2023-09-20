package com.couple.diary.presentation.member.dto

import com.couple.diary.application.member.dto.MemberJoinParam
import com.couple.diary.application.member.dto.MemberLoginParam
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Pattern

data class MemberJoinRequest(
    @field:Email(message = "이메일 형식이 아닙니다.")
    @field:NotBlank(message = "이메일을 입력해주세요.")
    val email: String,
    @field:NotBlank(message = "패스워드를 입력해주세요.")
    val password: String,
    @field:NotBlank(message = "이름을 입력해주세요.")
    val name: String,
    @field:NotBlank(message = "전화번호를 입력해주세요.")
    val phone: String,
    @field:Past(message = "생년월일을 제대로 입력해주세요.")
    @field:NotBlank(message = "생년월일을 입력해주세요.")
    val birth: String,
    @field:NotBlank(message = "성별을 입력해주세요.")
    @field:Pattern(regexp = "^(WOMAN|MAN)\$")
    val gender: String,
    val profileImage: String?
) {
    fun toParam() = MemberJoinParam(
        email, password, name, phone, birth, gender, profileImage
    )
}

data class MemberLoginRequest(
    @field:Email(message = "이메일 형식이 아닙니다.")
    @field:NotBlank(message = "이메일을 입력해주세요.")
    val email: String,
    @field:NotBlank(message = "패스워드를 입력해주세요.")
    val password: String
) {
    fun toParam() = MemberLoginParam(
        email, password
    )
}