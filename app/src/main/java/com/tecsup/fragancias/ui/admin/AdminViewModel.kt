package com.tecsup.fragancias.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.fragancias.AppContainer
import com.tecsup.fragancias.data.repository.ItemRepository
import com.tecsup.fragancias.notification.Notificador
import com.tecsup.fragancias.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class AdminStats(
    val totalGuardado: Int,
    val totalFavoritos: Int
)

class AdminViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<AdminStats>>(UiState.Cargando)
    val estado: StateFlow<UiState<AdminStats>> = _estado.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        viewModelScope.launch {
            repo.items.collect {
                cargarTotales()
            }
        }
    }

    fun cargarTotales() {
        viewModelScope.launch {
            val total = repo.cantidadGuardada()
            val favoritos = repo.cantidadFavoritos()
            _estado.value = UiState.Exito(
                AdminStats(
                    totalGuardado = total,
                    totalFavoritos = favoritos
                )
            )
        }
    }

    fun sincronizar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            repo.refrescar()
                .onSuccess { n ->
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                    _mensaje.value = "Se sincronizaron $n elementos"
                    cargarTotales()
                }
                .onFailure { e ->
                    _mensaje.value = when (e) {
                        is IOException -> "No hay conexión a internet"
                        is HttpException -> "El servidor respondió con error ${e.code()}"
                        else -> "No se pudo sincronizar"
                    }
                    cargarTotales()
                }
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            repo.limpiarCache()
            _mensaje.value = "Caché limpiada"
            cargarTotales()
        }
    }

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AdminViewModel(
                        repo = container.itemRepository,
                        notificador = container.notificador
                    ) as T
                }
            }
    }
}
