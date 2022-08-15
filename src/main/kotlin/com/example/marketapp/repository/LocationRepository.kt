package com.example.marketapp.repository

import com.example.marketapp.model.Address
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Suppress("SpringDataRepositoryMethodReturnTypeInspection")
@Repository
interface LocationRepository : CoroutineCrudRepository<Address, Int> {


    suspend fun findByUserId(userId: UUID): Address?

    @Modifying
    @Query("DELETE FROM address WHERE id = $1")
    suspend fun delete(id: Int): Int

}