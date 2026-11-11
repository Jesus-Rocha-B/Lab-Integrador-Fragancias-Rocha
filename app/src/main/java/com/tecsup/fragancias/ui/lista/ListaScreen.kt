package com.tecsup.fragancias.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.fragancias.ui.UiState
import com.tecsup.fragancias.ui.components.Cargando
import com.tecsup.fragancias.ui.components.ErrorView
import com.tecsup.fragancias.ui.components.FraganciaCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    onItemClick: (Int) -> Unit = {},
    onFavoritosClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Fragancias") },
                actions = {
                    TextButton(onClick = onFavoritosClick) {
                        Text(text = "Favoritos")
                    }
                    TextButton(onClick = viewModel::refrescar) {
                        Text(text = "Refrescar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            aviso?.let { mensajeAviso ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = mensajeAviso,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (val actual = estado) {
                    is UiState.Cargando -> Cargando()
                    is UiState.Error -> ErrorView(
                        mensaje = actual.mensaje,
                        onReintentar = viewModel::refrescar
                    )
                    is UiState.Exito -> {
                        if (actual.datos.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No hay fragancias disponibles",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(
                                    items = actual.datos,
                                    key = { it.id }
                                ) { fragancia ->
                                    FraganciaCard(
                                        item = fragancia,
                                        onClick = { onItemClick(fragancia.id) },
                                        onAlternarFavorito = { viewModel.alternarFavorito(fragancia.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
