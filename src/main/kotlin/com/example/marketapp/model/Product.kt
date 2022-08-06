package com.example.marketapp.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Table("product")
data class Product(
    @field:NotBlank @Column("name") val name: String,
    @field:Min(0) @Column("price") val price: Float,
    @Column("likes") var like: Int = 0,
    @Column("avalible") var avalible: Boolean = true,
    @field:JsonIgnore @Column("createdat") val createdAt: LocalDateTime = LocalDateTime.now(),
    @field:NotBlank @Column("description") var description: String,
    @field:NotNull @Column("category_fk") val categoryId: Int,
) {
    @Id
    @Column("id")
    var id: Int? = null

    @Transient
    var images: List<String>? = null

    @Transient
    var date: String? = createdAt.format(DateTimeFormatter.ISO_DATE_TIME)

    constructor() : this("", 0f, description = "", categoryId = -1)

}
