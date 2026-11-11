package com.tecsup.fragancias.data.repository

import com.tecsup.fragancias.data.local.FavoritoEntity
import com.tecsup.fragancias.data.local.ItemDao
import com.tecsup.fragancias.data.mapper.aEntity
import com.tecsup.fragancias.data.mapper.aModelo
import com.tecsup.fragancias.data.remote.ApiService
import com.tecsup.fragancias.domain.model.Item
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao,
    private val uidFlow: Flow<String>,
    private val obtenerUid: () -> String
) : ItemRepository {

    private val idsFavoritosFlow: Flow<Set<Int>> = uidFlow
        .flatMapLatest { uid ->
            if (uid.isBlank()) {
                flowOf(emptyList())
            } else {
                dao.observarIdsFavoritos(uid)
            }
        }
        .map { it.toSet() }

    override val items: Flow<List<Item>> = combine(
        dao.todos(),
        idsFavoritosFlow
    ) { lista, favoritosIds ->
        lista.map { it.aModelo(esFavorito = it.id in favoritosIds) }
    }

    override val favoritos: Flow<List<Item>> = items.map { lista ->
        lista.filter { it.esFavorito }
    }

    override suspend fun refrescar(): Result<Int> = try {
        val nuevos = api.listar().products.map { it.aEntity() }
        dao.guardarTodos(nuevos)
        Result.success(nuevos.size)
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        val uid = obtenerUid()
        val esFav = if (uid.isBlank()) false else dao.esFavorito(uid, id)
        val entidad = api.obtenerDetalle(id).aEntity(esFavorito = esFav)
        dao.guardarUno(entidad)
        Result.success(entidad.aModelo(esFavorito = esFav))
    } catch (e: IOException) {
        val uid = obtenerUid()
        val esFav = if (uid.isBlank()) false else dao.esFavorito(uid, id)
        dao.porId(id)?.let { Result.success(it.aModelo(esFavorito = esFav)) } ?: Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) {
        val uid = obtenerUid()
        if (uid.isBlank()) return
        if (dao.esFavorito(uid, id)) {
            dao.quitarFavorito(uid, id)
        } else {
            dao.agregarFavorito(FavoritoEntity(usuarioId = uid, itemId = id))
        }
    }

    override suspend fun cantidadGuardada() = dao.contar()

    override suspend fun cantidadFavoritos(): Int {
        val uid = obtenerUid()
        return if (uid.isBlank()) 0 else dao.contarFavoritos(uid)
    }

    override suspend fun limpiarCache() = dao.borrarTodo()
}
