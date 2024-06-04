package com.store.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Product @JsonCreator constructor(
    @JsonProperty("id") val id: Int = 0,
    @JsonProperty("name") val name: String?,
    @JsonProperty("type") val type: ProductType?,
    @JsonProperty("inventory") val inventory: Int?,
    @JsonProperty("cost") val cost: Double?
)
