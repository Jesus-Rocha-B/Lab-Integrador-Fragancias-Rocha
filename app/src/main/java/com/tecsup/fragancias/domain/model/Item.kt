package com.tecsup.fragancias.domain.model

data class Item(
    val id: Int,
    val titulo: String,
    val marca: String,
    val precio: Double,
    val rating: Double,
    val stock: Int,
    val descripcion: String,
    val imagenUrl: String?,
    val esFavorito: Boolean = false,
    val descuento: Double = 0.0,
    val categoria: String = "",
    val disponibilidad: String = "",
    val garantia: String = "",
    val envio: String = "",
    val politicaDevolucion: String = ""
)
