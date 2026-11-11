package com.tecsup.fragancias.data.local

import androidx.room.Entity

@Entity(
    tableName = "favoritos_usuario",
    primaryKeys = ["usuarioId", "itemId"]
)
data class FavoritoEntity(
    val usuarioId: String,
    val itemId: Int
)
