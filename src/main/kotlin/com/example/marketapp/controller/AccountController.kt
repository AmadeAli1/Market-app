package com.example.marketapp.controller

import com.example.marketapp.extra.ValidationRequest
import com.example.marketapp.model.User
import com.example.marketapp.service.UserService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("/api/account")
@RestController
class AccountController(
    private val userService: UserService
) {
    @PostMapping(
        "/register", consumes = [MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    suspend fun save(@Valid @RequestBody user: User): ResponseEntity<out Any> {
        return userService.save(user = user)
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

    @PostMapping(
        "/profile", consumes = [
            MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE
        ]
    )
    suspend fun changeProfilePicture(
        @RequestPart("file") file: FilePart,
        @RequestParam("userId", required = true) id: String,
    ): ResponseEntity<out Any> {
        val linkDownload = userService.save(image = file)
        return userService.changeProfilePicture(userId = id, imageUrl = linkDownload)
    }

    @DeleteMapping("/profile")
    suspend fun deleteProfile(
        @RequestParam("userId", required = true) id: String,
    ): ResponseEntity<out Any> {
        return userService.changeProfilePicture(userId = id, imageUrl = null)
    }

}