package com.example.marketapp.repository

import com.example.marketapp.model.Category
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : CoroutineCrudRepository<Category, Int>