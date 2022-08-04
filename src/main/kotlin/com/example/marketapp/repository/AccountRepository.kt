@file:Suppress("SpringDataRepositoryMethodReturnTypeInspection")

package com.example.marketapp.repository

import com.example.marketapp.model.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository : CoroutineCrudRepository<User, UUID> {
    suspend fun existsByEmail(email: Flow<String>): Boolean

    suspend fun findByEmail(email: Flow<String>): User?
}