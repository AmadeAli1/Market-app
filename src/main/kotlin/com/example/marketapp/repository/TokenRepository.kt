package com.example.marketapp.repository

import com.example.marketapp.model.Token
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Suppress("SpringDataRepositoryMethodReturnTypeInspection")
@Repository
interface TokenRepository : CoroutineCrudRepository<Token, UUID> {

    suspend fun findByUidAndValid(uid: UUID, valid: Boolean): Token?

}