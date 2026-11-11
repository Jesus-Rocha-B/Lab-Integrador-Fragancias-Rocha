package com.tecsup.fragancias.data.mapper

import com.tecsup.fragancias.data.local.ItemEntity
import com.tecsup.fragancias.data.remote.dto.ItemDto
import com.tecsup.fragancias.domain.model.Item

fun ItemDto.SaEntity(esFavorito: Boolean = false): ItemEntity = ItemEntity(
    id = id,
    titulo = title,
    marca = brand.orEmpty(),
    precio = price,
    rating = rating,
    stock = stock,
    descripcion = description,
    imagenUrl = thumbnail ?: images.firstOrNull(),
    esFavorito = esFavorito,
    descuento = discountPercentage,
    categoria = category,
    disponibilidad = availabilityStatus,
    garantia = warrantyInformation,
    envio = shippingInformation,
    politicaDevolucion = returnPolicy
)

fun ItemEntity.aModelo(): Item = Item(
    id = id,
    titulo = titulo,
    marca = marca,
    precio = precio,
    rating = rating,
    stock = stock,
    descripcion = descripcion,
    imagenUrl = imagenUrl,
    esFavorito = esFavorito,
    descuento = descuento,
    categoria = categoria,
    disponibilidad = disponibilidad,
    garantia = garantia,
    envio = envio,
    politicaDevolucion = politicaDevolucion
)
