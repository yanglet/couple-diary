package com.couple.diary.domain.member

import com.couple.diary.domain.base.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "MEMBER")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var memberNo: Long = 0,

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var phone: String,

    @Column(nullable = false)
    var birth: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var gender: Gender,

    var profileImage: String? = null

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        if (memberNo != other.memberNo) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (phone != other.phone) return false
        if (birth != other.birth) return false
        if (gender != other.gender) return false
        if (profileImage != other.profileImage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = memberNo.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + phone.hashCode()
        result = 31 * result + birth.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + (profileImage?.hashCode() ?: 0)
        return result
    }
}