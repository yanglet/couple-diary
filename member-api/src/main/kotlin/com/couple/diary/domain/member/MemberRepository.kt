package com.couple.diary.domain.member

interface MemberRepository {
    fun save(member: Member): Member?
    fun findByIdOrNull(memberNo: Long): Member?
}