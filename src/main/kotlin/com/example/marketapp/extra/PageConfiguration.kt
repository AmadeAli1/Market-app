package com.example.marketapp.extra

import com.example.marketapp.repository.ProductRepository
import com.example.marketapp.response.Page

class PageConfiguration<T> {

    suspend fun config(
        repository: ProductRepository,
        page: Int,
        onComplete: suspend (total: Long, paginas: Int, start: Int) -> Page<T>,
    ): Page<T> {
        val limit = 20.0
        val totalItems = repository.count()
        val paginas = totalItems.div(limit).plus(1).toInt()
        val start = if (page == 1) {
            0
        } else {
            (page - 1) * limit.toInt()
        }
        return onComplete(totalItems, paginas, start)
    }

    fun getPage(data: List<T>, paginas: Int, totalItems: Long, page: Int, hasNext: Boolean): Page<T> {
        return Page<T>(
            data = data,
            pageSize = data.size,
            pageNumber = page,
            totalPages = paginas,
            totalItems = totalItems,
            maxPageSize = 20,
            nextPage = if (!hasNext) null else page + 1,
            prevPage = if (page == 1) null else page - 1
        )
    }


}