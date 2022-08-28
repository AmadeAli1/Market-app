@file:Suppress("SpringDataRepositoryMethodReturnTypeInspection")

package com.example.marketapp.repository.business

import com.example.marketapp.model.business.Product
import com.example.marketapp.model.business.ShoppingCart
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ShoppingRepository : CoroutineCrudRepository<ShoppingCart, Int> {

    @Query("SELECT * FROM shoppingcart WHERE product_fk=$1 AND user_fk=$2")
    suspend fun findByProductIdAndUserId(productId: Int, userId: UUID): ShoppingCart?

    @Query("SELECT * FROM product p join shoppingcart s on p.id = s.product_fk where user_fk=$1")
    fun findByUserId(userId: UUID): Flow<Product>

    @Query("SELECT * FROM shoppingcart where user_fk=$1")
    fun findAllById(userId: UUID): Flow<ShoppingCart>

    @Modifying
    @Query("DELETE FROM shoppingcart WHERE id=$1")
    suspend fun removeById(id: Int): Int

}