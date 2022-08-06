package com.example.marketapp.extra

import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : CoroutineCrudRepository<Image, Int> {

    @Modifying
    @Query("DELETE from image where id=$1")
    suspend fun removeById(id: Int): Int
}