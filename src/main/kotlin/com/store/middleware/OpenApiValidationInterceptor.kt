package com.store.middleware

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.openapi4j.parser.OpenApi3Parser
import org.openapi4j.parser.model.v3.OpenApi3
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.openapi4j.operation.validator.model.Request
import org.openapi4j.operation.validator.model.impl.Body
import org.openapi4j.operation.validator.model.impl.DefaultRequest
import org.openapi4j.operation.validator.validation.RequestValidator
import org.springframework.web.util.ContentCachingRequestWrapper
import java.io.BufferedReader
import java.io.File
import java.io.InputStream

@Component
class OpenApiValidationInterceptor : HandlerInterceptor {

    private val openAPI: OpenApi3
    private val requestValidator: RequestValidator

    init {
//        val inputStream: InputStream = javaClass.classLoader.getResourceAsStream("products_api.yaml")
//        openAPI = OpenApi3Parser().parse(inputStream, true)
//        requestValidator = RequestValidator(openAPI)
//        val file = File("products_api.yaml")
//        openAPI = OpenApi3Parser().parse(file, true)
//        requestValidator = RequestValidator(openAPI)
        openAPI = OpenApi3Parser().parse(File("products_api.yaml"), true)
        requestValidator = RequestValidator(openAPI)
    }

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val cachingRequest = ContentCachingRequestWrapper(request)
//        val inputStream = cachingRequest.inputStream
//        val body = inputStream.bufferedReader().use(BufferedReader::readText)
//        val body = String(cachingRequest.contentAsByteArray, Charsets.UTF_8)
        println("Request: ${cachingRequest.requestURI}")
        println("Request: ${cachingRequest.method}")
        println("Request: ${cachingRequest.contentType}")
//        println("Request: ${body}")
        val apiRequest = convertToOpenApiRequest(cachingRequest)
        val validator = requestValidator.getValidator(apiRequest)
//        validator.validateBody(apiRequest,)

        println("API Request: ${apiRequest.body}")
//        val requestParameters = requestValidator.validate(apiRequest)
//        if (requestParameters.pathParameters?.isEmpty() != false &&
//            requestParameters.queryParameters?.isEmpty() != false &&
//            requestParameters.headerParameters?.isEmpty() != false &&
//            requestParameters.cookieParameters?.isEmpty() != false) {
//            response.status = HttpServletResponse.SC_BAD_REQUEST
//            response.writer.write("Validation error: No parameters found.")
//            return false
//        }

        return true
    }


    private fun convertToOpenApiRequest(request: ContentCachingRequestWrapper): Request {
        val requestBody = String(request.contentAsByteArray)
        val headers = mutableMapOf<String, Collection<String>>()
//        val method = Request.Method.valueOf(request.method.toUpperCase())
        val path = request.requestURI
//        val headers = request.headerNames.toList().associateWith { request.getHeaders(it).toList() }
        val queryParameters = request.parameterMap.mapValues { it.value.toList() }
//        val body = if (request.inputStream != null) Body.from(request.inputStream) else null

        val headerNames = request.headerNames
        while (headerNames.hasMoreElements()) {
            val headerName = headerNames.nextElement()
            headers[headerName] = listOf(request.getHeader(headerName))
        }

        val method = Request.Method.valueOf(request.method.toUpperCase())

        // Parse the request body as a JSON object
        val mapper = jacksonObjectMapper()
        val jsonNode = mapper.readTree(requestBody)
        println("JSON Node: $requestBody")
        // Iterate over the JSON object to check for null values
        jsonNode.fields().forEach {
            println("Key: ${it.key}, Value: ${it.value}")
            if (it.value.isNull) {
                throw IllegalArgumentException("Request body should not contain null values")
            }
        }

        val body = Body.from(requestBody)
        println("Body: $body")

        return DefaultRequest.Builder(request.requestURI, method)
            .headers(headers)
            .body(body)
            .build()
    }
}





