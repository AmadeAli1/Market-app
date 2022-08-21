package com.example.marketapp.controller

import com.example.marketapp.model.business.Category
import com.example.marketapp.service.business.CategoryService
import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/category")
@RestController
class CategoryController(
    private val categoryService: CategoryService,
) {

    @GetMapping
    suspend fun findAll(): Flow<Category> {
        return categoryService.findAll()
    }

    @PostMapping(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.APPLICATION_JSON_VALUE
        ]
    )
    suspend fun save(
        @RequestPart("file", required = true) image: FilePart,
        @RequestPart("name", required = true) name: String,
    ): ResponseEntity<out Any> {
        if (name.isBlank()) {
            ResponseEntity.badRequest().body("Name must not be null or empty")
        }
        return try {
            val type = Category.CategoryType.valueOf(value = name)
            val categoria = categoryService.save(category = Category(name = type), file = image)
            return ResponseEntity(categoria, HttpStatus.CREATED)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body("This Category $name is invalid!")
        }
    }

}