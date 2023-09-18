package com.couple.diary.domain.base

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    lateinit var insertedDate: LocalDateTime
        protected set

    @CreatedBy
    @Column(updatable = false)
    lateinit var insertedOperator: String
        protected set

    @LastModifiedDate
    lateinit var updatedDate: LocalDateTime
        protected set

    @LastModifiedBy
    lateinit var updatedOperator: String
        protected set

    var deleted: Boolean = false
        protected set

    var deletedDate: LocalDateTime? = null
        protected set

    fun delete() {
        this.deleted = true
        this.deletedDate = LocalDateTime.now()
    }
}