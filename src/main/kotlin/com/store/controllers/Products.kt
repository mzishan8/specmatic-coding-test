package com.store.controllers

import com.store.model.Product
import com.store.model.ProductType
import com.store.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class Products(val productService: ProductService) {

    @GetMapping()
    fun getProducts(@RequestParam(name = "type", required = false) type: ProductType?): List<Product> {
        return productService.getProductsByType(type)
    }

    @PostMapping()
    fun addProduct(
        @RequestBody product: Product
    ): Any {
        if (isValidProduct(product))
            return ResponseEntity.status(HttpStatus.CREATED).body(mapOf("id" to productService.saveProduct(product)))
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "Invalid product"))
    }

    private fun isValidProduct(product: Product): Boolean {
        if (product.inventory == null || product.type == null || product.name == null || product.cost == null)
            return false
        return isValidString(product.name)  && isValidString(product.type.name)
    }

    private fun isValidString(value: String?): Boolean {
        return value != null && value.matches(Regex("^[a-zA-Z\\s]+$")) && value !in listOf("true", "false")
    }
}
