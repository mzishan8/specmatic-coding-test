package com.store.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.store.model.ErrorResponseBody
import org.openapi4j.core.validation.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(exception: ValidationException): ResponseEntity<String> {
        return ResponseEntity("Validation error: ${exception.message}", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<Any> {
        val errors = ex.bindingResult.allErrors.map { error ->
            (error as FieldError).field + " " + error.defaultMessage
        }
        val errorResponseBody = ErrorResponseBody(
            error = errors.joinToString(", "),
            status = HttpStatus.BAD_REQUEST.value(),
            timestamp = java.time.OffsetDateTime.now().toLocalDateTime().toString(),
            path = "/products"
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponseBody)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleHttpMessageNotReadableException(ex: RuntimeException): ResponseEntity<Any> {
        println("Exception: $ex")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                ErrorResponseBody(
                    error = "Invalid request body",
                    status = HttpStatus.BAD_REQUEST.value(),
                    timestamp = java.time.OffsetDateTime.now().toLocalDateTime().toString(),
                    path = "/products"
                )
            )
    }
}