package com.tecsup.fragancias.data.repository

import com.tecsup.fragancias.data.local.ItemDao
import com.tecsup.fragancias.data.mapper.aEntity
import com.tecsup.fragancias.data.mapper.aModelo
import com.tecsup.fragancias.data.remote.ApiService
import com.tecsup.fragancias.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao
) : ItemRepository {

    override val items: Flow<List<Item>> = dao.todos().map { lista ->
        lista.map { it.aModelo() }
    }

    override val favoritos: Flow<List<Item>> = dao.favoritos().map { lista ->
        lista.map { it.aModelo() }
    }

    override suspend fun refrescar(): Result<Int> = try {
        val favoritos = dao.idsFavoritos().toSet()
        val nuevos = api.listar().products.map { it.aEntity(esFavorito = it.id in favoritos) }
        dao.guardarTodos(nuevos)
        Result.success(nuevos.size)
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        val esFav = dao.porId(id)?.esFavorito ?: false
        val entidad = api.obtenerDetalle(id).aEntity(esFavorito = esFav)
        dao.guardarUno(entidad)
        Result.success(entidad.aModelo())
    } catch (e: IOException) {
        dao.porId(id)?.let { Result.success(it.aModelo()) } ?: Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) = dao.alternarFavorito(id)

    override suspend fun cantidadGuardada() = dao.contar()

    override suspend fun cantidadFavoritos() = dao.contarFavoritos()

    override suspend fun limpiarCache() = dao.borrarTodo()
}
