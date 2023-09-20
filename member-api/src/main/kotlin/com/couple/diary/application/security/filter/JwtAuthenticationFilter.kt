package com.couple.diary.application.security.filter

import com.couple.diary.application.common.Log
import com.couple.diary.application.security.JwtService
import com.couple.diary.application.security.PrincipalDetailsService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val principalDetailsService: PrincipalDetailsService
) : OncePerRequestFilter(), Log {
    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val accessToken = request.getHeader(HEADER_AUTHORIZATION)

            if (StringUtils.hasText(accessToken) && jwtService.isValidatedToken(accessToken)) {
                val email = jwtService.getEmailByAccessToken(accessToken)
                val principal = principalDetailsService.loadUserByUsername(email)
                val authentication =
                    UsernamePasswordAuthenticationToken(principal, null, principal.authorities)
                        .also { it.details = WebAuthenticationDetailsSource().buildDetails(request) }

                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            log.error("Could not set user authentication in security context", e)
        }

        filterChain.doFilter(request, response)
    }
}