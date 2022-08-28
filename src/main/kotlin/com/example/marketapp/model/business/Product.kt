package com.example.marketapp.model.business

import com.example.marketapp.response.ProductDTO
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
    @field:NotBlank @Column("duration") var duration: String,
    @field:NotBlank @Column("calories") var calories: Int,
    @field:JsonIgnore @Column("createdat") val createdAt: LocalDateTime = LocalDateTime.now(),
    @field:NotBlank @Column("description") var description: String,
    @field:NotNull @Column("category_fk") val categoryId: Int,
) {
    @Id
    @Column("id")
    var id: Int? = null

    @Transient
    var images: List<String>? = null

    fun toProductDTO(category: Category): ProductDTO {
        return ProductDTO(
            id = id!!,
            category = category,
            name = name,
            price = price,
            like = like,
            images = images!!,
            description = description,
            avalible = avalible,
            date = createdAt.format(DateTimeFormatter.ISO_DATE_TIME)
        )
    }

    constructor() : this("", 0f, description = "", categoryId = -1, duration = "1-2", calories = 100)

}
