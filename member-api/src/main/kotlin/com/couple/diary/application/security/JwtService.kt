package com.couple.diary.application.security

import com.couple.diary.application.common.extension.toLong
import com.couple.diary.application.member.exception.MemberNotFoundException
import com.couple.diary.application.security.exception.TokenInvalidException
import com.couple.diary.domain.member.Member
import com.couple.diary.domain.member.MemberRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class JwtService(
    @Value("\${secret-key}")
    private val secretKey: String,
    @Value("\${expiration-hours}")
    private val expirationHours: Long,
    @Value("\${refresh-expiration-hours}")
    private val refreshExpirationHours: Long,
    @Value("\${issuer}")
    private val issuer: String,

    private val memberRepository: MemberRepository
) {
    fun createAccessToken(member: Member): String {
        val key = getKey()
        val now = Date()
        val expirationDate = getExpirationDate()

        val accessToken = Jwts
            .builder()
            .setSubject(member.email)
            .setIssuedAt(now)
            .setExpiration(expirationDate)
            .claim("memberNo", member.memberNo)
            .claim("issuer", issuer)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
        return accessToken
    }

    fun recreateAccessTokenByRefreshToken(refreshToken: String): String {
        validateToken(refreshToken)

        val claims = getClaims(refreshToken)

        validateIssuer(claims["issuer"].toString())

        val memberNo = claims["memberNo"].toLong()
        val member = memberRepository.findByIdOrNull(memberNo)
            ?: throw MemberNotFoundException("회원을 찾을 수 없습니다.")

        val accessToken = createAccessToken(member)
        return accessToken
    }

    fun createRefreshToken(member: Member): String {
        val key = getKey()
        val now = Date()
        val expirationDate = getRefreshExpirationDate()

        val refreshToken = Jwts
            .builder()
            .setSubject(member.email)
            .setIssuedAt(now)
            .setExpiration(expirationDate)
            .claim("memberNo", member.memberNo)
            .claim("issuer", issuer)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
        return refreshToken
    }

    fun getEmailByAccessToken(accessToken: String): String = getClaims(accessToken).subject

    fun isValidatedToken(token: String): Boolean {
        val result = kotlin.runCatching { getClaims(token) }
        return result.isSuccess
    }

    private fun validateToken(token: String) {
        val validated = isValidatedToken(token)
        if (!validated) {
            throw TokenInvalidException("토큰이 유효하지 않습니다.")
        }
    }

    private fun validateIssuer(issuer: String) {
        val same = issuer == this.issuer
        if (!same) {
            throw TokenInvalidException("토큰이 유효하지 않습니다.")
        }
    }

    private fun getClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .body

    private fun getKey() = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))

    private fun getExpirationDate() = Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS))

    private fun getRefreshExpirationDate() = Date.from(Instant.now().plus(refreshExpirationHours, ChronoUnit.HOURS))
}