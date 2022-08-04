package com.example.marketapp.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Table("Usuario")
data class User(
    @NotBlank @Email @Column("email") val email: String,
    @NotBlank @Column("username") val username: String,
    @NotBlank @Column("password") var password: String,
    @NotBlank @Column("address") val address: String? = null,
    @Column("postalCode") val postalCode: String? = null,
    @Column("imageUrl") var imageUrl: String? = null,
    @Column("isOpen") var isOpen: Boolean = false,
) {
    @Id
    @Column("uid")
    lateinit var uid: UUID
}
