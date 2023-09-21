package com.couple.diary.application.auth

import com.couple.diary.application.auth.dto.TokenResponse
import com.couple.diary.application.security.JwtService
import com.couple.diary.domain.member.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val jwtService: JwtService
) {
    fun getTokens(member: Member): TokenResponse {
        val accessToken = jwtService.createAccessToken(member)
        val refreshToken = jwtService.createRefreshToken(member)
        return TokenResponse(accessToken, refreshToken)
    }

    @Transactional(readOnly = true)
    fun getAccessTokenByRefreshToken(refreshToken: String) =
        jwtService.recreateAccessTokenByRefreshToken(refreshToken)
}
