package com.example.marketapp.controller

import com.example.marketapp.model.business.ShoppingCart
import com.example.marketapp.response.ApiResponse
import com.example.marketapp.response.ShoppingCartDTO
import com.example.marketapp.service.business.ShoppingService
import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("/api/shopping")
@RestController
class ShoppingController(private val service: ShoppingService) {

    @PostMapping("/add")
    suspend fun add(@Valid @RequestBody shoppingCart: ShoppingCart): ResponseEntity<ApiResponse<String>> {
        val cart = service.addToShoppingCart(shoppingCart)
        return ResponseEntity(cart, HttpStatus.CREATED)
    }

    @GetMapping("/user/{id}")
    suspend fun findAll(@PathVariable("id", required = true) id: String): Flow<ShoppingCartDTO> {
        return service.findShoppingCart(id)
    }

    @DeleteMapping("/{id}")
    suspend fun remove(@PathVariable("id", required = true) id: Int): ResponseEntity<ApiResponse<String>> {
        val cart = service.deleteShoppingCart(id)
        val message = if (cart) {
            "Item removed from shopping cart"
        } else {
            "Failed to remove the item"
        }
        return ResponseEntity.ok(ApiResponse(message))
    }


}