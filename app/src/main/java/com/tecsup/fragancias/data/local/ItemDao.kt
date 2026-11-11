package com.tecsup.fragancias.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM items ORDER BY titulo")
    fun todos(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun porId(id: Int): ItemEntity?

    @Query("SELECT itemId FROM favoritos_usuario WHERE usuarioId = :usuarioId")
    fun observarIdsFavoritos(usuarioId: String): Flow<List<Int>>

    @Query("SELECT EXISTS(SELECT 1 FROM favoritos_usuario WHERE usuarioId = :usuarioId AND itemId = :itemId)")
    suspend fun esFavorito(usuarioId: String, itemId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun agregarFavorito(favorito: FavoritoEntity)

    @Query("DELETE FROM favoritos_usuario WHERE usuarioId = :usuarioId AND itemId = :itemId")
    suspend fun quitarFavorito(usuarioId: String, itemId: Int)

    @Query("SELECT COUNT(*) FROM items")
    suspend fun contar(): Int

    @Query("SELECT COUNT(*) FROM favoritos_usuario f INNER JOIN items i ON f.itemId = i.id WHERE f.usuarioId = :usuarioId")
    suspend fun contarFavoritos(usuarioId: String): Int

    @Upsert
    suspend fun guardarTodos(items: List<ItemEntity>)

    @Upsert
    suspend fun guardarUno(item: ItemEntity)

    @Query("DELETE FROM items")
    suspend fun borrarTodo()
}
