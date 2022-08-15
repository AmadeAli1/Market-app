package com.example.marketapp.controller

import com.example.marketapp.extra.ValidationRequest
import com.example.marketapp.model.Product
import com.example.marketapp.response.Page
import com.example.marketapp.service.ProductService
import com.fasterxml.jackson.databind.json.JsonMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@RequestMapping("/api/product")
@RestController
class ProductController(
    private val productService: ProductService,
    private val validate: ValidationRequest,
) {
    @PostMapping(
        consumes = [
            MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE
        ]
    )
    suspend fun save(
        @RequestPart("file") images: Flux<FilePart>,
        @RequestPart("body") body: String,
    ): ResponseEntity<out Any> = withContext(Dispatchers.IO) {
        val request = JsonMapper().readValue(body, Product::class.java)
        val validateRequest = validate.validateRequest(request)

        if (validateRequest != null) {
            return@withContext validateRequest
        }
        val produto = productService.save(files = images.toStream().toList(), request = request)
        ResponseEntity(produto, HttpStatus.CREATED)
    }

    @GetMapping
    suspend fun findWithPagination(
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("name", required = false, defaultValue = "") name: String,
    ): ResponseEntity<Page<Product>> {
        val response: Page<Product> = if (name.isBlank()) {
            productService.findByPage(page = page)
        } else {
            productService.findByNameWithPagination(page = page, name = name)
        }
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/category")
    suspend fun findProductByCategory(
        @RequestParam(name = "id", required = true) category: Int,
    ): ResponseEntity<Flow<Product>> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(productService.searchByCategoria(categoryId = category))
    }


}