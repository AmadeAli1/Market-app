@file:Suppress("SpringDataRepositoryMethodReturnTypeInspection")

package com.example.marketapp.repository

import com.example.marketapp.model.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository : CoroutineCrudRepository<User, UUID> {
    suspend fun existsByEmail(email: String): Boolean

    suspend fun findByEmail(email:String): User?
}