package com.store.controllers

import com.store.model.Product
import com.store.model.ProductDetails
import com.store.model.ProductType
import com.store.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/products")
class Products(val productService: ProductService) {

    @GetMapping()
    fun getProducts(@RequestParam(name = "type", required = false) type: ProductType?): List<Product> {
        return productService.getProductsByType(type)
    }

    @PostMapping()
    fun addProduct(
        @Valid @RequestBody product: ProductDetails
    ): Any {
        if (!isValidString(product.name)) throw MethodArgumentNotValidException(null, null)
        return ResponseEntity.status(HttpStatus.CREATED).body(mapOf("id" to productService.saveProduct(product)))
    }

    private fun isValidString(value: String?): Boolean {
        return value != null && value.matches(Regex("^[a-zA-Z\\s]+$")) && value !in listOf("true", "false")
    }
}

