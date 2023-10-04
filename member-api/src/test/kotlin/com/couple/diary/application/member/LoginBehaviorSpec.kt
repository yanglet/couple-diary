package com.couple.diary.application.member

import com.couple.diary.application.auth.AuthService
import com.couple.diary.application.member.exception.MemberNotFoundException
import com.couple.diary.application.member.exception.PasswordNotMatchedException
import com.couple.diary.domain.member.Gender
import com.couple.diary.domain.member.Member
import com.couple.diary.domain.member.MemberRepository
import com.couple.diary.helper.member.UnitTestHelper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.password.PasswordEncoder

class LoginBehaviorSpec : BehaviorSpec({
    val authService = mockk<AuthService>()
    val memberRepository = mockk<MemberRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val memberService = MemberService(authService, memberRepository, passwordEncoder)

    Given("회원가입 되어 있지 않은 회원일 때") {
        val email = "yanglet1@gmail.com"
        val password = "password"

        every { memberRepository.findByEmail(email) } answers { null }

        When("로그인을 시도하면") {
            val memberLoginParam = UnitTestHelper.createMemberLoginParam(email, password)

            Then("MemberNotFoundException 이 발생한다.") {
                shouldThrow<MemberNotFoundException> { memberService.login(memberLoginParam) }

                verify(exactly = 0) {
                    passwordEncoder.matches(any(), any())
                    authService.getTokens(any())
                }
                verify(exactly = 1) {
                    memberRepository.findByEmail(email)
                }
            }
        }
    }

    Given("회원가입 되어 있는 회원일 때") {
        val email = "yanglet2@gmail.com"
        val password = "notMatchedPassword"
        val member = Member(
            email = email,
            password = "password1",
            name = "양글렛",
            gender = Gender.MAN,
            phone = "",
            birth = "1998-02-28"
        )

        every { memberRepository.findByEmail(email) } answers { member }
        every { passwordEncoder.matches(password, member.password) } answers { false }

        When("비밀번호가 일치하지 않는 로그인 요청이 오면") {
            val memberLoginParam = UnitTestHelper.createMemberLoginParam(email, password)

            Then("PasswordNotMatchedException 이 발생한다.") {
                shouldThrow<PasswordNotMatchedException> { memberService.login(memberLoginParam) }

                verify(exactly = 0) {
                    authService.getTokens(any())
                }
                verify(exactly = 1) {
                    memberRepository.findByEmail(email)
                    passwordEncoder.matches(password, member.password)
                }
            }
        }
    }

    Given("회원가입이 되어 있는 회원일 때") {
        val email = "yanglet3@gmail.com"
        val password = "password2"
        val member = Member(
            email = email,
            password = "password2",
            name = "양글렛",
            gender = Gender.MAN,
            phone = "",
            birth = "1998-02-28"
        )

        every { memberRepository.findByEmail(email) } answers { member }
        every { passwordEncoder.matches(password, member.password) } answers { true }
        every { authService.getTokens(member) } answers {
            UnitTestHelper.createTokenResponse("accessToken", "refreshToken")
        }

        When("정상 로그인 요청이 오면") {
            val memberLoginParam = UnitTestHelper.createMemberLoginParam(email, password)

            Then("토큰이 발급된다.") {
                memberService.login(memberLoginParam)

                verify(exactly = 1) {
                    memberRepository.findByEmail(email)
                    passwordEncoder.matches(password, member.password)
                    authService.getTokens(member)
                }
            }
        }
    }
})