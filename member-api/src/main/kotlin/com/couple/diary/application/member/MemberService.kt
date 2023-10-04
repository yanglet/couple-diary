package com.couple.diary.application.member

import com.couple.diary.application.auth.AuthService
import com.couple.diary.application.common.Log
import com.couple.diary.application.member.dto.MemberJoinParam
import com.couple.diary.application.member.dto.MemberLoginParam
import com.couple.diary.application.member.dto.MemberLoginResponse
import com.couple.diary.application.member.dto.MemberReadResponse
import com.couple.diary.application.member.exception.EmailDuplicatedException
import com.couple.diary.application.member.exception.MemberNotFoundException
import com.couple.diary.application.member.exception.PasswordNotMatchedException
import com.couple.diary.application.member.extension.toReadResponse
import com.couple.diary.domain.member.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val authService: AuthService,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) : Log {
    @Transactional
    fun join(param: MemberJoinParam): Long {
        validateDuplicatedEmail(param.email)

        val encodingPassword = passwordEncoder.encode(param.password)
        val gender = Gender.valueOf(param.gender)
        val memberRole = MemberRole(role = Role.MEMBER)

        val member = Member(
            email = param.email,
            password = encodingPassword,
            name = param.name,
            phone = param.phone,
            birth = param.birth,
            gender = gender,
            profileImage = param.profileImage
        ).also { it.addRole(memberRole) }
        memberRepository.save(member)

        return member.memberNo
    }

    @Transactional
    fun login(param: MemberLoginParam): MemberLoginResponse {
        val member = memberRepository.findByEmail(param.email) ?: throw MemberNotFoundException("찾을 수 없는 회원입니다.")

        val matched = passwordEncoder.matches(param.password, member.password)
        when {
            matched -> {
                val tokenResponse = authService.getTokens(member)
                return MemberLoginResponse(
                    accessToken = tokenResponse.accessToken,
                    refreshToken = tokenResponse.refreshToken
                )
            }
            else -> throw PasswordNotMatchedException("비밀번호가 일치하지 않습니다.")
        }
    }

    @Transactional(readOnly = true)
    fun readMember(memberNo: Long): MemberReadResponse =
        memberRepository.findByIdOrNull(memberNo)?.toReadResponse()
            ?: throw MemberNotFoundException("찾을 수 없는 회원입니다.")

    private fun validateDuplicatedEmail(email: String) {
        val exists = memberRepository.existsByEmail(email)
        if (exists) {
            throw EmailDuplicatedException("중복되는 이메일입니다.")
        }
    }
}