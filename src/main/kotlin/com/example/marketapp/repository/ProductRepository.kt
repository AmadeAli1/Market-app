package com.example.marketapp.repository

import com.example.marketapp.model.Product
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : CoroutineCrudRepository<Product, Int> {

    @Query("INSERT INTO imageproduct (product_fk,imageurl) values(:?1,:?2)")
    @Modifying
    suspend fun imageToProduct(product: Int, imageUrl: String): Int

    @Query("SELECT imageurl from imageproduct where product_fk=$1 ")
    suspend fun getImagesToProduct(product: Int): Flow<String>

    @Query("select * from product limit 20 offset :start")
    fun findByPage(start: Int): Flow<Product>

    @Query("select * from product where upper(product.name) like upper(concat($1,'%')) order by name")
    fun searchByName(query: String): Flow<Product>

    fun findAllByCategoryId(categoryId: Int): Flow<Product>

    //fun searchAllByNameIsLikeIgnoreCase(name: String): Flow<Product>
}