package com.example.marketapp.service.business

import com.example.marketapp.exception.ApiException
import com.example.marketapp.extra.ImageService
import com.example.marketapp.extra.PageConfiguration
import com.example.marketapp.model.business.Product
import com.example.marketapp.repository.business.CategoryRepository
import com.example.marketapp.repository.business.ProductRepository
import com.example.marketapp.response.ProductDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val imageService: ImageService,
    private val categoryRepository: CategoryRepository,
    private val repository: ProductRepository,
) {
    suspend fun save(files: MutableList<FilePart>, request: Product): Product {
        lateinit var product: Product
        lateinit var imagesUrls: List<String>
        try {
            product = repository.save(entity = request)
            imagesUrls = files.map {
                val imageUrl: String = imageService.save(it)!!
                val status = repository.imageToProduct(product = product.id!!, imageUrl = imageUrl)
                if (status != 1) {
                    throw ApiException("Image with name <${it.filename()}> failed to upload")
                }
                imageUrl
            }
        } catch (e: Exception) {
            if (product.id != null) {
                repository.deleteById(product.id!!)
            }
            throw ApiException(e.message!!)
        }
        product.images = imagesUrls
        return product
    }

    suspend fun deleteById(id: Int) {
        try {
            repository.deleteById(id)
        } catch (e: Exception) {
            throw ApiException("Failed to delete this product!")
        }
    }

    suspend fun searchByCategoria(page: Int, categoryId: Int, name: String) = withContext(Dispatchers.IO) {
        val pageConfiguration = PageConfiguration<ProductDTO>()
        lateinit var data: List<ProductDTO>
        pageConfiguration.config(repository = repository, page) { total, paginas, start ->
            data = if (name.isBlank()) {
                repository.findAllByCategory(start = start, categoryId = categoryId).map(this@ProductService::mapper)
                    .map(this@ProductService::mapperDTO)
                    .toList()
            } else {
                repository.findAllByNameAnAndCategory(
                    start = start, name = name, categoryId = categoryId
                ).map(this@ProductService::mapper).map(this@ProductService::mapperDTO).toList()
            }
            val next = (data.size == 20).and(total > page * 20)
            return@config pageConfiguration.getPage(
                data = data,
                paginas = paginas,
                totalItems = total,
                page = page,
                hasNext = next
            )
        }
    }

    private suspend fun mapperDTO(product: Product): ProductDTO {
        val category = categoryRepository.findById(product.categoryId)!!
        return product.toProductDTO(category)
    }

    suspend fun findByPage(page: Int) = withContext(Dispatchers.IO) {
        val pageConfiguration = PageConfiguration<ProductDTO>()
        lateinit var data: List<ProductDTO>
        pageConfiguration.config(repository = repository, page) { total, paginas, start ->
            data = repository.findPage(start = start)
                .map(this@ProductService::mapper)
                .map {
                    val category = categoryRepository.findById(it.categoryId)!!
                    it.toProductDTO(category)
                }.toList()
            val next = (data.size == 20).and(total > page * 20)
            return@config pageConfiguration.getPage(
                data = data,
                paginas = paginas,
                totalItems = total,
                page = page,
                hasNext = next
            )
        }
    }

    suspend fun findByNameWithPagination(page: Int, name: String) = withContext(Dispatchers.IO) {
        val pageConfiguration = PageConfiguration<ProductDTO>()
        lateinit var data: List<ProductDTO>
        pageConfiguration.config(repository = repository, page) { total, paginas, start ->
            data = repository.findAllByName(start = start, name = name).map(::mapper).map(::mapperDTO).toList()
            val next = (data.size == 20).and(total > page * 20)
            return@config pageConfiguration.getPage(
                data = data,
                paginas = paginas,
                totalItems = total,
                page = page,
                hasNext = next
            )
        }
    }

    private suspend fun mapper(product: Product): Product {
        val imagesUrl = repository.getImagesToProduct(product = product.id!!)
        product.images = imagesUrl.toList()
        return product
    }

}