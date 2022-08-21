package com.example.marketapp.model.client

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Table("address")
data class Address(
    @field:NotBlank @Column("name") var addressName: String,
    @field:NotNull var latitude: Double,
    @field:NotNull var longitude: Double,
    @field:NotNull @Column("userid") val userId: UUID,
) {
    @Id
    @Column("id")
    var id: Int? = null
}