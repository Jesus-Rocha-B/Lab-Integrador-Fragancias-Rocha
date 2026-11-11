package com.tecsup.fragancias.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.fragancias.AppContainer
import com.tecsup.fragancias.data.repository.ItemRepository
import com.tecsup.fragancias.domain.model.Item
import com.tecsup.fragancias.notification.Notificador
import com.tecsup.fragancias.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

enum class RangoPrecio(val etiqueta: String) {
    TODOS("Todo precio"),
    MENOR_70("< $70"),
    ENTRE_70_100("$70 - $100"),
    MAYOR_100("> $100")
}

enum class OrdenPrecio(val etiqueta: String) {
    NINGUNO("Relevancia"),
    MENOR_A_MAYOR("Menor precio"),
    MAYOR_A_MENOR("Mayor precio")
}

class ListaViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _marcasSeleccionadas = MutableStateFlow<Set<String>>(emptySet())
    val marcasSeleccionadas: StateFlow<Set<String>> = _marcasSeleccionadas.asStateFlow()

    private val _rangoPrecio = MutableStateFlow(RangoPrecio.TODOS)
    val rangoPrecio: StateFlow<RangoPrecio> = _rangoPrecio.asStateFlow()

    private val _ordenPrecio = MutableStateFlow(OrdenPrecio.NINGUNO)
    val ordenPrecio: StateFlow<OrdenPrecio> = _ordenPrecio.asStateFlow()

    private val _marcasDisponibles = MutableStateFlow<List<String>>(emptyList())
    val marcasDisponibles: StateFlow<List<String>> = _marcasDisponibles.asStateFlow()

    private val _hayFiltrosActivos = MutableStateFlow(false)
    val hayFiltrosActivos: StateFlow<Boolean> = _hayFiltrosActivos.asStateFlow()

    private val _itemsBase = MutableStateFlow<List<Item>>(emptyList())
    private var refrescoTerminado = false
    private var ultimoRefrescoExitoso = false

    init {
        viewModelScope.launch {
            repo.items.collect { lista ->
                _itemsBase.value = lista
                val disponibles = lista
                    .mapNotNull { it.marca.takeIf { marca -> marca.isNotBlank() } }
                    .distinct()
                    .sorted()
                _marcasDisponibles.value = disponibles
                if (disponibles.isNotEmpty() && _marcasSeleccionadas.value.size >= disponibles.size &&
                    disponibles.all { it in _marcasSeleccionadas.value }
                ) {
                    _marcasSeleccionadas.value = emptySet()
                }
                if (
                    lista.isNotEmpty() ||
                    (refrescoTerminado && ultimoRefrescoExitoso) ||
                    (_estado.value is UiState.Exito && lista.isEmpty())
                ) {
                    publicarFiltrados()
                }
            }
        }
        refrescar()
    }

    fun refrescar() {
        viewModelScope.launch {
            val actual = _estado.value
            if (actual is UiState.Error || (_itemsBase.value.isEmpty() && actual is UiState.Exito)) {
                _estado.value = UiState.Cargando
            }
            val resultado = repo.refrescar()
            refrescoTerminado = true
            resultado
                .onSuccess { n ->
                    ultimoRefrescoExitoso = true
                    if (_estado.value is UiState.Cargando) {
                        publicarFiltrados()
                    }
                    _aviso.value = null
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                }
                .onFailure { e ->
                    ultimoRefrescoExitoso = false
                    if (repo.cantidadGuardada() == 0) {
                        _aviso.value = null
                        _estado.value = UiState.Error(mensajeDe(e))
                    } else {
                        _aviso.value = "Sin conexión: mostrando datos guardados"
                    }
                }
        }
    }

    fun onBusquedaChange(texto: String) {
        _busqueda.value = texto
        actualizarSiHayDatos()
    }

    fun toggleMarca(marca: String) {
        val actuales = _marcasSeleccionadas.value
        val nuevas = if (marca in actuales) actuales - marca else actuales + marca
        val disponibles = _marcasDisponibles.value
        _marcasSeleccionadas.value = if (
            disponibles.isNotEmpty() &&
            nuevas.size >= disponibles.size &&
            disponibles.all { it in nuevas }
        ) {
            emptySet()
        } else {
            nuevas
        }
        actualizarSiHayDatos()
    }

    fun seleccionarTodasMarcas() {
        _marcasSeleccionadas.value = emptySet()
        actualizarSiHayDatos()
    }

    fun seleccionarRangoPrecio(rango: RangoPrecio) {
        _rangoPrecio.value = if (_rangoPrecio.value == rango) RangoPrecio.TODOS else rango
        actualizarSiHayDatos()
    }

    fun seleccionarOrdenPrecio(orden: OrdenPrecio) {
        _ordenPrecio.value = if (_ordenPrecio.value == orden) OrdenPrecio.NINGUNO else orden
        actualizarSiHayDatos()
    }

    fun limpiarFiltrosPrecio() {
        _rangoPrecio.value = RangoPrecio.TODOS
        _ordenPrecio.value = OrdenPrecio.NINGUNO
        actualizarSiHayDatos()
    }

    fun limpiarFiltros() {
        _busqueda.value = ""
        _marcasSeleccionadas.value = emptySet()
        _rangoPrecio.value = RangoPrecio.TODOS
        _ordenPrecio.value = OrdenPrecio.NINGUNO
        actualizarSiHayDatos()
    }

    fun alternarFavorito(id: Int) = viewModelScope.launch {
        repo.alternarFavorito(id)
    }

    private fun actualizarSiHayDatos() {
        actualizarEstadoFiltros()
        if (_estado.value is UiState.Exito || _itemsBase.value.isNotEmpty()) {
            publicarFiltrados()
        }
    }

    private fun actualizarEstadoFiltros() {
        _hayFiltrosActivos.value = _busqueda.value.isNotBlank() ||
            _marcasSeleccionadas.value.isNotEmpty() ||
            _rangoPrecio.value != RangoPrecio.TODOS ||
            _ordenPrecio.value != OrdenPrecio.NINGUNO
    }

    private fun publicarFiltrados() {
        actualizarEstadoFiltros()
        val consulta = _busqueda.value.trim().lowercase()
        val marcas = _marcasSeleccionadas.value
        val rango = _rangoPrecio.value
        val orden = _ordenPrecio.value

        var lista = _itemsBase.value.filter { item ->
            val coincideTexto = consulta.isEmpty() ||
                item.titulo.lowercase().contains(consulta) ||
                item.marca.lowercase().contains(consulta)
            val coincideMarca = marcas.isEmpty() ||
                marcas.any { it.equals(item.marca, ignoreCase = true) }
            val coincidePrecio = when (rango) {
                RangoPrecio.TODOS -> true
                RangoPrecio.MENOR_70 -> item.precio < 70.0
                RangoPrecio.ENTRE_70_100 -> item.precio in 70.0..100.0
                RangoPrecio.MAYOR_100 -> item.precio > 100.0
            }
            coincideTexto && coincideMarca && coincidePrecio
        }

        lista = when (orden) {
            OrdenPrecio.NINGUNO -> lista
            OrdenPrecio.MENOR_A_MAYOR -> lista.sortedBy { it.precio }
            OrdenPrecio.MAYOR_A_MENOR -> lista.sortedByDescending { it.precio }
        }

        _estado.value = UiState.Exito(lista)
    }

    private fun mensajeDe(e: Throwable) = when (e) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${e.code()}"
        else -> "Ocurrió un error inesperado"
    }

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ListaViewModel(
                        repo = container.itemRepository,
                        notificador = container.notificador
                    ) as T
                }
            }
    }
}
