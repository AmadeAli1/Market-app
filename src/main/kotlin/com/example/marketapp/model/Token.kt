package com.example.marketapp.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Table("Token")
data class Token(
    @Column("createdAt") val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column("confirmedAt") var confirmedAt: LocalDateTime? = null,
    @Column("expiredAt") val expiredAt: LocalDateTime = createdAt.plus(2L, ChronoUnit.HOURS),
    @Column("isValid") var valid: Boolean = true,
    @Column("userId") val userId: UUID,
) {
    @Id
    @Column("uid")
    lateinit var uid: UUID
}
