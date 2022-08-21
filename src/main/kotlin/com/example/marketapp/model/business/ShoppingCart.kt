package com.example.marketapp.model.business

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Table("shoppingcart")
data class ShoppingCart(
    @field:NotNull @Column("product_fk") val productId: Int,
    @field:NotNull @Column("user_fk") val userId: UUID,
    @field:NotNull @field:Min(1) @Column("quantity") var quantity: Int,
    @field:NotNull @field:Min(1) @Column("unitprice") val unitPrice: Float,
) {
    @Id
    @Column("id")
    var id: Int? = null

}
