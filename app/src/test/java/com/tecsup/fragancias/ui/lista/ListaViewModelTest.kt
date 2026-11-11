package com.tecsup.fragancias.ui.lista

import com.tecsup.fragancias.data.repository.ItemRepository
import com.tecsup.fragancias.domain.model.Item
import com.tecsup.fragancias.notification.Notificador
import com.tecsup.fragancias.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

class ListaViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fraganciasMuestra = listOf(
        Item(
            id = 1,
            titulo = "Bleu de Chanel",
            marca = "Chanel",
            precio = 125.0,
            rating = 4.8,
            stock = 12,
            descripcion = "Aroma amaderado",
            imagenUrl = "https://example.com/1.png"
        ),
        Item(
            id = 2,
            titulo = "Dior Sauvage",
            marca = "Dior",
            precio = 89.0,
            rating = 4.7,
            stock = 18,
            descripcion = "Notas frescas",
            imagenUrl = "https://example.com/2.png"
        ),
        Item(
            id = 3,
            titulo = "Gucci Bloom",
            marca = "Gucci",
            precio = 59.0,
            rating = 4.5,
            stock = 9,
            descripcion = "Floral suave",
            imagenUrl = "https://example.com/3.png"
        )
    )

    @Test
    fun inicioExitoso_publicaItemsMarcasYNotifica() = runTest {
        val repo = FakeItemRepository(itemsRemotos = fraganciasMuestra)
        val notificador = FakeNotificador()

        val viewModel = ListaViewModel(repo = repo, notificador = notificador)

        val estado = viewModel.estado.value as UiState.Exito
        assertEquals(3, estado.datos.size)
        assertNull(viewModel.aviso.value)
        assertEquals(listOf("Chanel", "Dior", "Gucci"), viewModel.marcasDisponibles.value)
        assertEquals(1, notificador.registros.size)
        assertEquals("Datos actualizados" to "Se guardaron 3 elementos", notificador.registros.first())
    }

    @Test
    fun errorDeRedSinCache_muestraErrorYNoNotifica() = runTest {
        val repo = FakeItemRepository(
            errorRefresco = IOException("Sin red")
        )
        val notificador = FakeNotificador()

        val viewModel = ListaViewModel(repo = repo, notificador = notificador)

        val estado = viewModel.estado.value as UiState.Error
        assertEquals("No hay conexión a internet", estado.mensaje)
        assertNull(viewModel.aviso.value)
        assertTrue(notificador.registros.isEmpty())
    }

    @Test
    fun errorDeRedConCache_conservaListaYMuestraAvisoOffline() = runTest {
        val repo = FakeItemRepository(
            itemsIniciales = fraganciasMuestra,
            errorRefresco = IOException("Timeout")
        )
        val notificador = FakeNotificador()

        val viewModel = ListaViewModel(repo = repo, notificador = notificador)

        val estado = viewModel.estado.value as UiState.Exito
        assertEquals(3, estado.datos.size)
        assertEquals("Sin conexión: mostrando datos guardados", viewModel.aviso.value)
        assertTrue(notificador.registros.isEmpty())
    }

    @Test
    fun busquedaYSeleccionDeTodasLasMarcas_filtranYRestablecenATodos() = runTest {
        val repo = FakeItemRepository(itemsRemotos = fraganciasMuestra)
        val notificador = FakeNotificador()
        val viewModel = ListaViewModel(repo = repo, notificador = notificador)

        viewModel.onBusquedaChange("sauvage")
        var estado = viewModel.estado.value as UiState.Exito
        assertEquals(listOf("Dior Sauvage"), estado.datos.map { it.titulo })
        assertTrue(viewModel.hayFiltrosActivos.value)

        viewModel.onBusquedaChange("")
        viewModel.toggleMarca("Dior")
        assertEquals(setOf("Dior"), viewModel.marcasSeleccionadas.value)
        estado = viewModel.estado.value as UiState.Exito
        assertEquals(listOf("Dior"), estado.datos.map { it.marca })

        viewModel.toggleMarca("Chanel")
        assertEquals(setOf("Dior", "Chanel"), viewModel.marcasSeleccionadas.value)
        estado = viewModel.estado.value as UiState.Exito
        assertEquals(2, estado.datos.size)

        viewModel.toggleMarca("Gucci")
        assertTrue(viewModel.marcasSeleccionadas.value.isEmpty())
        estado = viewModel.estado.value as UiState.Exito
        assertEquals(3, estado.datos.size)
        assertFalse(viewModel.hayFiltrosActivos.value)
    }

    @Test
    fun filtrosPrecioYOrdenamiento_filtranYOrdenanCorrectamente() = runTest {
        val repo = FakeItemRepository(itemsRemotos = fraganciasMuestra)
        val notificador = FakeNotificador()
        val viewModel = ListaViewModel(repo = repo, notificador = notificador)

        viewModel.seleccionarOrdenPrecio(OrdenPrecio.MAYOR_A_MENOR)
        var estado = viewModel.estado.value as UiState.Exito
        assertEquals(listOf(125.0, 89.0, 59.0), estado.datos.map { it.precio })

        viewModel.seleccionarRangoPrecio(RangoPrecio.ENTRE_70_100)
        estado = viewModel.estado.value as UiState.Exito
        assertEquals(listOf("Dior Sauvage"), estado.datos.map { it.titulo })

        viewModel.limpiarFiltrosPrecio()
        estado = viewModel.estado.value as UiState.Exito
        assertEquals(3, estado.datos.size)
        assertEquals(RangoPrecio.TODOS, viewModel.rangoPrecio.value)
        assertEquals(OrdenPrecio.NINGUNO, viewModel.ordenPrecio.value)
    }

    @Test
    fun alternarFavorito_actualizaEstadoDelProductoEnLista() = runTest {
        val repo = FakeItemRepository(itemsRemotos = fraganciasMuestra)
        val notificador = FakeNotificador()
        val viewModel = ListaViewModel(repo = repo, notificador = notificador)

        viewModel.alternarFavorito(2)

        val estado = viewModel.estado.value as UiState.Exito
        val sauvage = estado.datos.first { it.id == 2 }
        assertTrue(sauvage.esFavorito)
    }

    private class FakeItemRepository(
        itemsIniciales: List<Item> = emptyList(),
        private val itemsRemotos: List<Item> = itemsIniciales,
        private val errorRefresco: Throwable? = null
    ) : ItemRepository {

        private val _items = MutableStateFlow(itemsIniciales)

        override val items: Flow<List<Item>> = _items.asStateFlow()
        override val favoritos: Flow<List<Item>> = _items.map { lista ->
            lista.filter { it.esFavorito }
        }

        override suspend fun refrescar(): Result<Int> {
            errorRefresco?.let { return Result.failure(it) }
            val favoritosActuales = _items.value.filter { it.esFavorito }.map { it.id }.toSet()
            val actualizados = itemsRemotos.map { item ->
                item.copy(esFavorito = item.id in favoritosActuales)
            }
            _items.value = actualizados
            return Result.success(actualizados.size)
        }

        override suspend fun obtenerDetalle(id: Int): Result<Item> {
            val encontrado = _items.value.firstOrNull { it.id == id }
            return if (encontrado != null) {
                Result.success(encontrado)
            } else {
                Result.failure(NoSuchElementException())
            }
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

    private class FakeNotificador : Notificador {
        val registros = mutableListOf<Pair<String, String>>()

        override fun mostrar(titulo: String, texto: String) {
            registros.add(titulo to texto)
        }
    }
}
