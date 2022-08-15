package com.example.marketapp.service

import com.example.marketapp.exception.ApiException
import com.example.marketapp.extra.ImageService
import com.example.marketapp.extra.PageConfiguration
import com.example.marketapp.model.Product
import com.example.marketapp.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val imageService: ImageService,
    private val repository: ProductRepository,
) {

    @Value(value = "\${source.api.product.pagination}")
    val pagination: String? = null

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

    suspend fun searchByCategoria(categoryId: Int): Flow<Product> {
        return repository.findAllByCategoryId(categoryId = categoryId).map(this::mapper)
    }

    suspend fun seacrhByName(name: String): Flow<Product> {
        return repository.searchByName(query = name).map(this::mapper)
    }

    suspend fun findByPage(page: Int) = withContext(Dispatchers.IO) {
        val pageConfiguration = PageConfiguration<Product>()
        lateinit var data: List<Product>
        pageConfiguration.config(repository = repository, page) { total, paginas, start ->
            data = repository.findPage(start = start).map(this@ProductService::mapper).toList()
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
        val pageConfiguration = PageConfiguration<Product>()
        lateinit var data: List<Product>
        pageConfiguration.config(repository = repository, page) { total, paginas, start ->
            data = repository.findPageWithName(start = start, name = name).map(this@ProductService::mapper).toList()
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