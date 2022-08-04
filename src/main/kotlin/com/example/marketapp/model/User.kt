package com.example.marketapp.model

import org.hibernate.validator.constraints.Length
import org.springframework.data.annotation.Id
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
    @field:NotBlank @Column("address") val address: String? = null,
    @Column("postalCode") var postalCode: String? = null,
    @Column("imageUrl") var imageUrl: String? = null,
    @Column("isOpen") var isOpen: Boolean = false,
) {
    @Id
    @Column("uid")
    lateinit var uid: UUID
}
