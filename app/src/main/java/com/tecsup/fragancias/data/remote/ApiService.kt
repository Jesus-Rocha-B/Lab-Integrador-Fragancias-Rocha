package com.tecsup.fragancias.data.remote

import com.tecsup.fragancias.data.remote.dto.ItemDto
import com.tecsup.fragancias.data.remote.dto.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("products/category/fragrances")
    suspend fun listar(): ProductsResponseDto

    @GET("products/{id}")
    suspend fun obtenerDetalle(@Path("id") id: Int): ItemDto
}
