package com.example.marketapp.repository.business

import com.example.marketapp.model.business.Product
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository : CoroutineCrudRepository<Product, Int> {

    @Query("INSERT INTO imageproduct (product_fk,imageurl) values(:?1,:?2)")
    @Modifying
    suspend fun imageToProduct(product: Int, imageUrl: String): Int

    @Query("SELECT imageurl from imageproduct where product_fk=$1 ")
    suspend fun getImagesToProduct(product: Int): Flow<String>

    @Query("select * from product limit 20 offset :start")
    fun findPage(start: Int): Flow<Product>

    @Query("select * from product where upper(product.name) like upper(concat($2,'%')) order by product.name limit 20 offset :$1")
    fun findAllByName(start: Int, name: String): Flow<Product>

    @Query("select * from product where upper(product.name) like upper(concat($2,'%')) and category_fk=$3 order by product.name limit 20 offset :$1")
    fun findAllByNameAnAndCategory(start: Int, name: String, categoryId: Int): Flow<Product>

    @Query("select * from product where category_fk=$2 order by product.name limit 20 offset :$1")
    fun findAllByCategory(start: Int, categoryId: Int): Flow<Product>

}