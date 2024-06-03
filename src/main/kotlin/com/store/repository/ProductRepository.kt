package com.store.repository

import com.store.model.Product
import com.store.model.ProductType
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class ProductRepository {
    private val products = ConcurrentHashMap<Int, Product>()
    private var count = 1

    fun getAllProducts(): List<Product> {
        return products.values.toList()
    }

    fun findByType(type: ProductType): List<Product> {
        return products.values.filter { it.type == type }
    }

    fun saveProduct(product: Product): Product {
        val id = count
        products[id] = product.copy(id = id)
        count++
        return products[id]!!
    }
}