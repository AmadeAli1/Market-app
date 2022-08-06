package com.example.marketapp.service

import com.example.marketapp.extra.ImageService
import com.example.marketapp.model.Category
import com.example.marketapp.repository.CategoryRepository
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val repository: CategoryRepository,
    private val imageService: ImageService,
) {

    suspend fun save(category: Category, file: FilePart): Category {
        val imageUrl = imageService.save(file)
        category.imageUrl = imageUrl
        return repository.save(entity = category)
    }

    suspend fun findAll() = repository.findAll()

}