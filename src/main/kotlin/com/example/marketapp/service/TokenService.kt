package com.example.marketapp.service

import com.example.marketapp.model.Token
import com.example.marketapp.repository.AccountRepository
import com.example.marketapp.repository.TokenRepository
import com.example.marketapp.service.UserService.Companion.invalidBlock
import kotlinx.coroutines.flow.flowOf
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class TokenService(
    private val tokenRepository: TokenRepository,
    private val accountRepository: AccountRepository,
) {

    suspend fun save(userId: UUID): UUID {
        val token = tokenRepository.save(entity = Token(userId = userId))
        return token.uid
    }

    suspend fun confirmToken(tokenId: String): ResponseEntity<out Any> {
        val tokenUUID = UUID.fromString(tokenId)

        val token = tokenRepository.findByUidAndValid(
            uid = flowOf(tokenUUID),
            valid = flowOf(true)
        )
        if (token == null) {
            return invalidBlock("This token not exists")
        } else {
            if (token.confirmedAt != null) {
                return invalidBlock(
                    message = "The Token has already been confirmed!"
                )
            }
            val currentTimer = LocalDateTime.now()
            return if (token.expiredAt.isBefore(currentTimer)) {
                invalidBlock(message = "The Token has expired")
            } else {
                val user = accountRepository.findById(id = token.userId)!!
                user.isOpen = true
                token.confirmedAt = currentTimer
                token.valid = false
                accountRepository.save(user)
                tokenRepository.save(entity = token)
                ResponseEntity.ok("Successful account confirmation!")
            }
        }
    }


}