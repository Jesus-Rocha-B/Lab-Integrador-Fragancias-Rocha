package com.tecsup.fragancias.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponseDto(
    val products: List<ItemDto> = emptyList()
)

@Serializable
data class ItemDto(
    val id: Int,
    val title: String,
    val description: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val discountPercentage: Double = 0.0,
    val rating: Double = 0.0,
    val stock: Int = 0,
    val brand: String? = null,
    val sku: String = "",
    val availabilityStatus: String = "",
    val warrantyInformation: String = "",
    val shippingInformation: String = "",
    val returnPolicy: String = "",
    val thumbnail: String? = null,
    val images: List<String> = emptyList()
)
