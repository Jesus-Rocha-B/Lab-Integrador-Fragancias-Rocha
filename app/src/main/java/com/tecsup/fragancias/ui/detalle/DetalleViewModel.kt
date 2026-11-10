package com.tecsup.fragancias.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.fragancias.AppContainer
import com.tecsup.fragancias.data.repository.ItemRepository
import com.tecsup.fragancias.domain.model.Item
import com.tecsup.fragancias.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DetalleViewModel(
    private val repo: ItemRepository,
    private val itemId: Int
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val estado: StateFlow<UiState<Item>> = _estado.asStateFlow()

    init {
        cargarDetalle()
    }

    fun cargarDetalle() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            repo.obtenerDetalle(itemId)
                .onSuccess { item ->
                    _estado.value = UiState.Exito(item)
                }
                .onFailure { e ->
                    _estado.value = UiState.Error(mensajeDe(e))
                }
        }
    }

    fun alternarFavorito() {
        val actual = _estado.value
        if (actual is UiState.Exito) {
            viewModelScope.launch {
                repo.alternarFavorito(itemId)
                _estado.value = UiState.Exito(
                    actual.datos.copy(esFavorito = !actual.datos.esFavorito)
                )
            }
        }
    }

    private fun mensajeDe(e: Throwable) = when (e) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${e.code()}"
        else -> "Ocurrió un error inesperado"
    }

    companion object {
        fun factory(container: AppContainer, itemId: Int): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DetalleViewModel(
                        repo = container.itemRepository,
                        itemId = itemId
                    ) as T
                }
            }
    }
}
