package com.couple.diary.application.member

import com.couple.diary.application.auth.AuthService
import com.couple.diary.application.member.exception.EmailDuplicatedException
import com.couple.diary.domain.member.Member
import com.couple.diary.domain.member.MemberRepository
import com.couple.diary.helper.member.UnitTestHelper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.password.PasswordEncoder

class JoinBehaviorSpec : BehaviorSpec({
    val authService = mockk<AuthService>()
    val memberRepository = mockk<MemberRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val memberService = MemberService(authService, memberRepository, passwordEncoder)

    Given("회원가입 되어 있는 이메일일 때") {
        val email = "yanglet@gmail.com"

        every { memberRepository.existsByEmail(email = email) } answers { true }

        When("동일 이메일로 회원가입을 시도할 경우") {
            val memberJoinParam = UnitTestHelper.createMemberJoinParam(
                email = email,
                password = "rawPassword",
                name = "yanglet",
                phone = "010-1234-5678",
                birth = "1998-02-28",
                gender = "MAN",
                profileImage = null
            )

            Then("EmailDuplicatedException 이 발생한다.") {
                shouldThrow<EmailDuplicatedException> { memberService.join(memberJoinParam) }

                verify (exactly = 0) {
                    passwordEncoder.encode(any())
                    memberRepository.save(any())
                }
            }
        }
    }

    Given("회원가입이 되어 있지 않은 이메일일 때") {
        every { memberRepository.existsByEmail(any()) } answers { false }
        every { memberRepository.save(any()) } answers { mockk() }
        every { passwordEncoder.encode(any()) } answers { "encodingPassword" }

        When("회원 가입을 시도할 경우") {
            val memberJoinParam = UnitTestHelper.createMemberJoinParam(
                email = "yanglet@gmail.com",
                password = "rawPassword",
                name = "yanglet",
                phone = "010-1234-5678",
                birth = "1998-02-28",
                gender = "MAN",
                profileImage = null
            )

            Then("정상적으로 회원가입 로직이 실행된다.") {
                memberService.join(memberJoinParam)

                verify (exactly = 1) {
                    passwordEncoder.encode(any())
                    memberRepository.save(any())
                }
            }
        }
    }

    Given("성별 값이 MAN, WOMAN 이 아닐 때") {
        val gender = "M"
        val password = "test3"
        val member = mockk<Member>()

        every { memberRepository.existsByEmail(any()) } answers { false }
        every { memberRepository.save(member) } answers { mockk() }
        every { passwordEncoder.encode(password) } answers { "encodingPassword" }

        When("회원가입을 시도할 경우") {
            val memberJoinParam = UnitTestHelper.createMemberJoinParam(
                email = "yanglet@gmail.com",
                password = password,
                name = "yanglet",
                phone = "010-1234-5678",
                birth = "1998-02-28",
                gender = gender,
                profileImage = null
            )

            Then("IllegalArgumentException 이 발생한다.") {
                shouldThrow<IllegalArgumentException> { memberService.join(memberJoinParam) }

                verify (exactly = 0) { memberRepository.save(member) }
                verify (exactly = 1) { passwordEncoder.encode(password) }
            }
        }
    }
})