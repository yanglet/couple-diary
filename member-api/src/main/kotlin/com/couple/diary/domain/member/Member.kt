package com.couple.diary.domain.member

import com.couple.diary.domain.base.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Where

@Entity
@Table(name = "MEMBER")
@Where(clause = "deleted = 'false'")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var memberNo: Long = 0,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var phone: String,

    @Column(nullable = false)
    var birth: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var gender: Gender,

    var coupleNo: Long? = null,

    var profileImage: String? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var memberRoles: Set<MemberRole> = mutableSetOf()

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        if (memberNo != other.memberNo) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (name != other.name) return false
        if (phone != other.phone) return false
        if (birth != other.birth) return false
        if (gender != other.gender) return false
        if (coupleNo != other.coupleNo) return false
        if (profileImage != other.profileImage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = memberNo.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + phone.hashCode()
        result = 31 * result + birth.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + (coupleNo?.hashCode() ?: 0)
        result = 31 * result + (profileImage?.hashCode() ?: 0)
        return result
    }

    fun addRole(memberRole: MemberRole) = this.memberRoles.plus(memberRole)
}