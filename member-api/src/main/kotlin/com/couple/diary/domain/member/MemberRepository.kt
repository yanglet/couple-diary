package com.couple.diary.domain.member

interface MemberRepository {
    fun save(member: Member): Member
    fun findByIdOrNull(memberNo: Long): Member?
    fun findByEmail(email: String): Member?
    fun existsByEmail(email: String): Boolean
}