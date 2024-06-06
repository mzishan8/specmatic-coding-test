package com.store.service

import com.store.model.Product
import com.store.model.ProductDetails
import com.store.model.ProductType
import com.store.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun getProductsByType(type: String?): List<Any> {
        if (type == null)
            return productRepository.getAllProducts()
        return productRepository.findByType(type)
    }

    fun saveProduct(product: Map<String, Any>): Int {
        return productRepository.saveProduct(product)
    }
}