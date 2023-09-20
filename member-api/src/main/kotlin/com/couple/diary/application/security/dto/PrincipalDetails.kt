package com.couple.diary.application.security.dto

import com.couple.diary.domain.member.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class PrincipalDetails(
    private val member: Member
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        member.memberRoles
            .map { SimpleGrantedAuthority("ROLE_${it.role}") }
            .toMutableSet()

    override fun getPassword() = member.password

    override fun getUsername() = member.email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}