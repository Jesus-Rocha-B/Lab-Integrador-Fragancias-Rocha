package com.tecsup.fragancias.data.mapper

import com.tecsup.fragancias.data.remote.dto.ItemDto
import com.tecsup.fragancias.domain.model.Item

fun ItemDto.aModelo(esFavorito: Boolean = false): Item = Item(
    id = id,
    titulo = title,
    marca = brand.orEmpty(),
    precio = price,
    rating = rating,
    stock = stock,
    descripcion = description,
    imagenUrl = thumbnail ?: images.firstOrNull(),
    esFavorito = esFavorito
)
