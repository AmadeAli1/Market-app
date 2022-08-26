package com.example.marketapp.response

import com.example.marketapp.model.business.ShoppingCart

data class ShoppingCartDTO(
    val shoppingCart: ShoppingCart,
    val product: ProductDTO,
){

}