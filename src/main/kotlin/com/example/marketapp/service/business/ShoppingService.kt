package com.example.marketapp.service.business

import com.example.marketapp.exception.ApiException
import com.example.marketapp.model.business.Product
import com.example.marketapp.model.business.ShoppingCart
import com.example.marketapp.repository.business.CategoryRepository
import com.example.marketapp.repository.business.ProductRepository
import com.example.marketapp.repository.business.ShoppingRepository
import com.example.marketapp.response.ShoppingCartDTO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import java.util.*

@Service
class ShoppingService(
    private val repository: ShoppingRepository,
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
) {

    suspend fun addToShoppingCart(shoppingCart: ShoppingCart): String {
        val exists =
            repository.existsByProductIdAndUserId(productId = shoppingCart.productId, userId = shoppingCart.userId)
        //Create item
        println("Here 2")
        if (!exists) {
            repository.save(entity = shoppingCart)
            return CREATED
        }

        //Update Section
        val oldShoppingCart = repository.findById(shoppingCart.id!!)
        if (oldShoppingCart != null) {
            if (oldShoppingCart.quantity != shoppingCart.quantity || oldShoppingCart.unitPrice != shoppingCart.unitPrice) {
                shoppingCart.id = oldShoppingCart.id
                repository.save(entity = shoppingCart)
                return UPDATED
            }

        }
        return EMPTY
    }

    @OptIn(FlowPreview::class)
    suspend fun findShoppingCart(userId: String): Flow<ShoppingCartDTO> {
        val id = UUID.fromString(userId)

        val myCart = repository.findAllById(userId = id)
        val transform: suspend (value: Flow<ShoppingCartDTO>) -> Flow<ShoppingCartDTO> = {
            it
        }
        return myCart.map { shop ->
            repository.findByUserId(userId = id).map(::mapper).map { mapperDTO(it, shop) }
        }.flatMapConcat(transform)
    }

    suspend fun deleteShoppingCart(id: Int): Boolean {
        try {
            return repository.removeById(id) == 1
        } catch (e: Exception) {
            throw ApiException("An error occurred to remove item !")
        }
    }


    private suspend fun mapper(product: Product): Product {
        val imagesUrl = productRepository.getImagesToProduct(product = product.id!!)
        product.images = imagesUrl.toList()
        return product
    }

    private suspend fun mapperDTO(product: Product, shoppingCart: ShoppingCart): ShoppingCartDTO {
        val category = categoryRepository.findById(product.categoryId)!!
        val productDTO = product.toProductDTO(category)
        return ShoppingCartDTO(
            shoppingCart = shoppingCart,
            product = productDTO
        )
    }

    companion object {
        private val CREATED = "Item was added in shopping cart"
        private val UPDATED = "Item has been updated in shopping cart"
        private val EMPTY = ""
    }


}