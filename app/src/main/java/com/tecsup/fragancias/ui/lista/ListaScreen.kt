package com.tecsup.fragancias.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
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
    onCerrarSesion: () -> Unit = {},
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
                    TextButton(onClick = viewModel::refrescar) {
                        Text(text = "Refrescar")
                    }
                    TextButton(onClick = onCerrarSesion) {
                        Text(text = "Salir")
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = {}) {
                        Text(
                            text = "Catálogo",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    TextButton(onClick = onFavoritosClick) {
                        Text(text = "Favoritos")
                    }
                }
            }
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = mensajeAviso,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        TextButton(onClick = viewModel::refrescar) {
                            Text(text = "Reintentar")
                        }
                    }
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
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "No hay fragancias disponibles",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = viewModel::refrescar) {
                                    Text(text = "Recargar")
                                }
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
