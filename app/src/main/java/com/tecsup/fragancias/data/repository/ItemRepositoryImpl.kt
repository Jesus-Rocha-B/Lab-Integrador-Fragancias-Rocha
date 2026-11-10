package com.tecsup.fragancias.data.repository

import com.tecsup.fragancias.data.mapper.aModelo
import com.tecsup.fragancias.data.remote.ApiService
import com.tecsup.fragancias.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class ItemRepositoryImpl(
    private val api: ApiService
) : ItemRepository {

    private val _items = MutableStateFlow<List<Item>>(emptyList())

    override val items: Flow<List<Item>> = _items.asStateFlow()

    override val favoritos: Flow<List<Item>> = _items.map { lista ->
        lista.filter { it.esFavorito }
    }

    override suspend fun refrescar(): Result<Int> = try {
        val favoritosIds = _items.value.filter { it.esFavorito }.map { it.id }.toSet()
        val nuevos = api.listar().products.map { it.aModelo(esFavorito = it.id in favoritosIds) }
        _items.value = nuevos
        Result.success(nuevos.size)
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        val esFav = _items.value.firstOrNull { it.id == id }?.esFavorito ?: false
        Result.success(api.obtenerDetalle(id).aModelo(esFavorito = esFav))
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) {
        _items.value = _items.value.map { item ->
            if (item.id == id) item.copy(esFavorito = !item.esFavorito) else item
        }
    }

    override suspend fun cantidadGuardada(): Int = _items.value.size

    override suspend fun cantidadFavoritos(): Int = _items.value.count { it.esFavorito }

    override suspend fun limpiarCache() {
        _items.value = emptyList()
    }
}
