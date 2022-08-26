package com.example.marketapp.controller

import com.example.marketapp.model.client.Address
import com.example.marketapp.model.client.User
import com.example.marketapp.response.ApiResponse
import com.example.marketapp.service.client.UserService
import kotlinx.coroutines.flow.Flow
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("/api/account")
@RestController
class AccountController(
    private val userService: UserService,
) {
    @PostMapping(
        "/register", consumes = [MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    suspend fun save(@Valid @RequestBody user: User): ResponseEntity<out Any> {
        return userService.save(user = user)
    }

    @PutMapping("/location")
    suspend fun addLocation(@Valid @RequestBody address: Address): Address {
        return userService.updateLocation(address = address)
    }

    @GetMapping
    suspend fun findAll(): Flow<User> {
        return userService.findAll()
    }

    @GetMapping("/login")
    suspend fun login(
        @RequestParam("email", required = true) email: String,
        @RequestParam("password", required = true) password: String,
    ): ResponseEntity<out Any> {
        return userService.login(email, password)
    }

    @PutMapping("/changePassword")
    suspend fun changePassword(
        @RequestBody user: User,
        @RequestParam("password", required = true) password: String,
    ): ResponseEntity<User> {
        return userService.changePassword(user = user, password = password)
    }

    @GetMapping("/confirmation")
    suspend fun confirmAccount(@RequestParam("token", required = true) token: String): ResponseEntity<out Any> {
        return userService.confirmToken(token)
    }

    @PostMapping("/profile")
    suspend fun changeProfilePicture(
        @RequestParam("imageUrl", required = true) imageUrl: String,
        @RequestParam("userId", required = true) id: String,
    ): ResponseEntity<ApiResponse<String>> {
        return userService.changeProfilePicture(userId = id, imageUrl = imageUrl)
    }


    @DeleteMapping("/profile")
    suspend fun deleteProfile(
        @RequestParam("userId", required = true) id: String,
    ): ResponseEntity<ApiResponse<String>> {
        return userService.changeProfilePicture(userId = id, imageUrl = null)
    }

}