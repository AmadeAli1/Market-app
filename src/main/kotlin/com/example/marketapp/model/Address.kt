package com.example.marketapp.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*
import javax.validation.constraints.NotBlank

@Table("address")
data class Address(
    @field:NotBlank @Column("name") var addressName: String,
    @field:NotBlank var latitude: Double,
    @field:NotBlank var longitude: Double,
    @field:NotBlank @Column("userid") val userId: UUID,
) {
    @Id
    @Column("id")
    var id: Int? = null
}