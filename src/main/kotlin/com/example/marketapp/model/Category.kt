package com.example.marketapp.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import javax.validation.constraints.NotBlank

@Table("Category")
data class Category(
    @Column("name") @NotBlank val name: CategoryType,
    @Column("imageUrl") var imageUrl: String? = null,
) {

    @Id
    @Column("id")
    var id: Int? = null

    enum class CategoryType {
        Salted,
        Cakes,
        Cookies
    }
}
