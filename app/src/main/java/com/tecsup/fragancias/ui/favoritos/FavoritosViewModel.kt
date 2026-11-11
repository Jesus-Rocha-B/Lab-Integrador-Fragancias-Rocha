package com.tecsup.fragancias.ui.favoritos

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

class FavoritosViewModel(
    private val repo: ItemRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            repo.favoritos.collect { lista ->
                _estado.value = UiState.Exito(lista)
            }
        }
    }

    fun alternarFavorito(id: Int) = viewModelScope.launch {
        repo.alternarFavorito(id)
    }

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FavoritosViewModel(
                        repo = container.itemRepository
                    ) as T
                }
            }
    }
}
