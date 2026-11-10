package com.tecsup.fragancias.ui.lista

import androidx.lifecycle.ViewModel
import com.tecsup.fragancias.domain.model.Item
import com.tecsup.fragancias.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListaViewModel : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    init {
        cargarDatosPrueba()
    }

    fun cargarDatosPrueba() {
        _estado.value = UiState.Exito(fraganciasPrueba)
    }

    fun alternarFavorito(id: Int) {
        val actual = _estado.value
        if (actual is UiState.Exito) {
            val actualizados = actual.datos.map { item ->
                if (item.id == id) item.copy(esFavorito = !item.esFavorito) else item
            }
            _estado.value = UiState.Exito(actualizados)
        }
    }

    private companion object {
        val fraganciasPrueba = listOf(
            Item(
                id = 6,
                titulo = "Calvin Klein CK One",
                marca = "Calvin Klein",
                precio = 49.99,
                rating = 4.37,
                stock = 29,
                descripcion = "Fragancia unisex clásica con aroma fresco y limpio ideal para uso diario.",
                imagenUrl = "https://cdn.dummyjson.com/product-images/fragrances/calvin-klein-ck-one/thumbnail.webp"
            ),
            Item(
                id = 7,
                titulo = "Chanel Coco Noir Eau De",
                marca = "Chanel",
                precio = 129.99,
                rating = 4.26,
                stock = 58,
                descripcion = "Fragancia elegante con notas de pomelo, rosa y sándalo para ocasiones de noche.",
                imagenUrl = "https://cdn.dummyjson.com/product-images/fragrances/chanel-coco-noir-eau-de/thumbnail.webp"
            ),
            Item(
                id = 8,
                titulo = "Dior J'adore",
                marca = "Dior",
                precio = 89.99,
                rating = 3.8,
                stock = 98,
                descripcion = "Fragancia floral sofisticada con mezcla de ylang-ylang, rosa y jazmín.",
                imagenUrl = "https://cdn.dummyjson.com/product-images/fragrances/dior-j'adore/thumbnail.webp"
            ),
            Item(
                id = 9,
                titulo = "Dolce Shine Eau de",
                marca = "Dolce & Gabbana",
                precio = 69.99,
                rating = 3.96,
                stock = 4,
                descripcion = "Fragancia frutal vibrante con notas de mango, jazmín y maderas claras.",
                imagenUrl = "https://cdn.dummyjson.com/product-images/fragrances/dolce-shine-eau-de/thumbnail.webp"
            ),
            Item(
                id = 10,
                titulo = "Gucci Bloom Eau de",
                marca = "Gucci",
                precio = 79.99,
                rating = 2.74,
                stock = 91,
                descripcion = "Fragancia floral moderna con notas de nardo, jazmín y madreselva.",
                imagenUrl = "https://cdn.dummyjson.com/product-images/fragrances/gucci-bloom-eau-de/thumbnail.webp"
            )
        )
    }
}
