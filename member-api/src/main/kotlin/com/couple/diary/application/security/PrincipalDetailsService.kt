package com.couple.diary.application.security

import com.couple.diary.application.member.exception.MemberNotFoundException
import com.couple.diary.application.security.dto.PrincipalDetails
import com.couple.diary.domain.member.MemberRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class PrincipalDetailsService(
    private val memberRepository: MemberRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String) =
        memberRepository.findByEmail(username)?.let {
            PrincipalDetails(it)
        } ?: throw MemberNotFoundException("찾을 수 없는 회원입니다.")
}