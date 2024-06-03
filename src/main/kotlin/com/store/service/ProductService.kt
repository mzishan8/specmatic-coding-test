package com.store.service

import com.store.model.Product
import com.store.model.ProductType
import com.store.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(val productRepository: ProductRepository) {
    fun getProductsByType(type: ProductType?): List<Product> {
        if (type == null)
            return productRepository.getAllProducts()
        return productRepository.findByType(type)
    }

    fun saveProduct(product: Product): Int {
        return productRepository.saveProduct(product).id
    }
}