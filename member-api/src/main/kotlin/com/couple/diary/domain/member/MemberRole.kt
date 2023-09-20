package com.couple.diary.domain.member

import jakarta.persistence.*

@Entity
@Table(name = "MEMBER_ROLE")
class MemberRole(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var memberRoleNo: Long = 0,

    @Column(nullable = false)
    var role: Role

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MemberRole

        if (memberRoleNo != other.memberRoleNo) return false
        if (role != other.role) return false

        return true
    }

    override fun hashCode(): Int {
        var result = memberRoleNo.hashCode()
        result = 31 * result + role.hashCode()
        return result
    }
}