package com.example.marketapp.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("Category")
data class Category(
    @Column("name") val name: CategoryType?,
    @Column("imageUrl") var imageUrl: String?,
    @Column("visualization") var views: Int = 0,
    @Column("products") var totalProducts: Int = 0,
) {
    @Id
    @Column("id")
    var id: Int? = null

    constructor(name: CategoryType?) : this(name, null)

    enum class CategoryType {
        Cakes,
        Cookies,
        Pancakes,
        Donuts,
        Desserts;
    }
}
