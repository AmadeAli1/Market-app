package com.example.marketapp.service

import com.example.marketapp.exception.ApiException
import com.example.marketapp.exception.Message
import com.example.marketapp.extra.ImageService
import com.example.marketapp.model.Address
import com.example.marketapp.model.User
import com.example.marketapp.repository.AccountRepository
import com.example.marketapp.response.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val accountRepository: AccountRepository,
    private val tokenService: TokenService,
    private val emailService: EmailService,
    private val passwordEncoder: PasswordEncoder,
    private val locationService: LocationService,
    private val imageService: ImageService,
) {
    @Value(value = "\${source.api.token}")
    val confirmTokenUrl: String? = null

    @Value(value = "\${source.api.imageUrl}")
    val imageUrl: String? = null

    suspend fun save(user: User): ResponseEntity<out Any> {
        if (validateEmail(email = user.email)) {
            return invalidBlock("This email already exists", "email")
        }
        user.password = encondePassword(password = user.password)
        val saved = accountRepository.save(entity = user)
        val token = tokenService.save(saved.uid)
        sendConfirmationToken(user = saved, token = token)
        println(saved)
        return ResponseEntity.ok(saved)
    }

    suspend fun login(email: String, password: String): ResponseEntity<out Any> {
        val account = findByEmail(email = email) ?: return invalidBlock("Account not found", "email")
        println(account)
        if (decodePassword(password = password, encryptedPassword = account.password)) {
            val location = locationService.findUserLocation(account.uid.toString())
            account.address = location
            return ResponseEntity.ok(account)
        }
        return invalidBlock("Invalid Password", "password")
    }

    suspend fun update(user: User): User {
        return accountRepository.save(user)
    }

    suspend fun updateLocation(address: Address): Address {
        try {
            return locationService.save(address)
        } catch (e: Exception) {
            throw ApiException("An error occurred while update your location!")
        }
    }

    suspend fun changePassword(user: User, password: String): ResponseEntity<User> {
        if (decodePassword(password = password, encryptedPassword = user.password)) {
            return ResponseEntity.ok(user)
        }
        val newPassword = encondePassword(password = password)
        user.password = newPassword
        val update = update(user)
        return ResponseEntity(update, HttpStatus.CREATED)
    }

    suspend fun confirmToken(token: String): ResponseEntity<out Any> {
        return tokenService.confirmToken(tokenId = token)
    }

    suspend fun changeProfilePicture(userId: String, imageUrl: String?): ResponseEntity<ApiResponse> {
        val user = accountRepository.findById(UUID.fromString(userId))!!
        return if (imageUrl != null) {
            user.imageUrl = imageUrl
            update(user)
            println("Profile picture updated!")
            ResponseEntity.ok(ApiResponse("Profile picture updated!"))
        } else {
            user.imageUrl = null
            update(user)
            println("Profile picture removed!")
            ResponseEntity.ok(ApiResponse("Profile picture removed!"))
        }
    }

    suspend fun findAll(): Flow<User> {
        return accountRepository.findAll().map {
            it.address = locationService.findUserLocation(it.uid.toString())
            it
        }
    }

    suspend fun save(image: FilePart): String? {
        return imageService.save(file = image)
    }

    private suspend fun sendConfirmationToken(user: User, token: UUID) {
        emailService.sendEmail(
            sendToEmail = user.email, subject = "Confirm account", body = """
                        Hi ${user.username} thank you for your account at Market
                        To confirm your account: ${confirmTokenUrl}?token=${token}
                        Valid Token for 2 HOURS!
                    """.trimIndent()
        )
    }

    private suspend fun findByEmail(email: String): User? {
        return accountRepository.findByEmail(email = email)
    }

    private suspend fun validateEmail(email: String): Boolean {
        return accountRepository.existsByEmail(email = email)
    }

    private fun encondePassword(password: String): String {
        return passwordEncoder.encode(password)
    }

    private fun decodePassword(password: String, encryptedPassword: String): Boolean {
        return passwordEncoder.matches(password, encryptedPassword)
    }


    companion object {
        fun invalidBlock(message: String, field: String? = null) = ResponseEntity(
            Message(message = message, field = field),
            HttpStatus.BAD_REQUEST
        )
    }
}