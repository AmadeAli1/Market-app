package com.example.marketapp.response

import com.example.marketapp.model.business.Category

data class ProductDTO(
    val id: Int,
    val category: Category,
    val name: String,
    val price: Float,
    val like: Int,
    val avalible: Boolean,
    val date: String,
    val description: String,
    val images: List<String>,
)
