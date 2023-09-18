package com.couple.diary.infrastructure.member

import com.couple.diary.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository

interface JpaMemberRepository : JpaRepository<Member, Long> {
}