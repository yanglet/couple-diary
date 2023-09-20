package com.couple.diary.application.member.extension

import com.couple.diary.application.member.dto.MemberReadResponse
import com.couple.diary.domain.member.Member

fun Member.toReadResponse() = MemberReadResponse(
    memberNo = this.memberNo,
    email = this.email,
    name = this.name,
    phone = this.phone,
    birth = this.birth,
    gender = this.gender.toString(),
    coupleNo = this.coupleNo,
    profileImage = this.profileImage
)