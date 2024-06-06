package com.store.repository

import com.store.model.Product
import com.store.model.ProductDetails
import com.store.model.ProductType
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class ProductRepository {
    private val products = ConcurrentHashMap<Int, Map<String, Any?>>()
    private var count = 1

    fun getAllProducts(): List<Any> {
        return products.values.toList()
    }

    fun findByType(type: String): List<Any> {
        return products.values.filter { it["type"] == type }
    }

    fun saveProduct(product: Map<String, Any>): Int {
        val id = count
        println("It is here  === " + product.values)
        products[id] = product + ("id" to id)
        count++
        return id
    }
}