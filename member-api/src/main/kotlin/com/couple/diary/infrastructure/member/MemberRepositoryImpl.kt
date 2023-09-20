package com.couple.diary.infrastructure.member

import com.couple.diary.domain.member.Member
import com.couple.diary.domain.member.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl(
    private val jpaMemberRepository: JpaMemberRepository
) : MemberRepository {
    override fun save(member: Member) = jpaMemberRepository.save(member)
    override fun findByIdOrNull(memberNo: Long) = jpaMemberRepository.findByIdOrNull(memberNo)
    override fun findByEmail(email: String) = jpaMemberRepository.findByEmail(email)
    override fun existsByEmail(email: String) = jpaMemberRepository.existsByEmail(email)
}