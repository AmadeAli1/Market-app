package com.example.marketapp.model

import org.hibernate.validator.constraints.Length
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Table("Usuario")
data class User(
    @field:NotBlank @field:Email @Column("email") val email: String,
    @field:NotBlank @field:Column("username") val username: String,
    @field:NotBlank @field:Length(min = 6) @Column("password") var password: String,
    @Column("imageUrl") var imageUrl: String? = null,
    @Column("isOpen") var isVerified: Boolean = false,
) {
    @Id
    @Column("uid")
    lateinit var uid: UUID
    @Transient
    var address: Address? = null
}
