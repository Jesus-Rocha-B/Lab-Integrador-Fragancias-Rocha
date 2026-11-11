package com.tecsup.fragancias.ui.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.fragancias.domain.model.Item
import com.tecsup.fragancias.ui.UiState
import com.tecsup.fragancias.ui.components.Cargando
import com.tecsup.fragancias.ui.components.ErrorView
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val actual = estado) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(
                    mensaje = actual.mensaje,
                    onReintentar = viewModel::cargarDetalle
                )
                is UiState.Exito -> DetalleContenido(
                    item = actual.datos,
                    onAlternarFavorito = viewModel::alternarFavorito
                )
            }
        }
    }
}

@Composable
private fun DetalleContenido(
    item: Item,
    onAlternarFavorito: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = item.titulo,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        if (item.marca.isNotBlank()) {
            Text(
                text = item.marca,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = String.format(Locale.US, "$%.2f", item.precio),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            if (item.descuento > 0) {
                Text(
                    text = String.format(Locale.US, "%.1f%% dto.", item.descuento),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }

        Text(
            text = item.descripcion,
            style = MaterialTheme.typography.bodyLarge
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilaDetalle(etiqueta = "Rating", valor = item.rating.toString())
                FilaDetalle(etiqueta = "Stock", valor = item.stock.toString())
                if (item.categoria.isNotBlank()) {
                    FilaDetalle(etiqueta = "Categoría", valor = item.categoria)
                }
                if (item.disponibilidad.isNotBlank()) {
                    FilaDetalle(etiqueta = "Estado", valor = item.disponibilidad)
                }
                if (item.garantia.isNotBlank()) {
                    HorizontalDivider()
                    FilaDetalle(etiqueta = "Garantía", valor = item.garantia)
                }
                if (item.envio.isNotBlank()) {
                    FilaDetalle(etiqueta = "Envío", valor = item.envio)
                }
                if (item.politicaDevolucion.isNotBlank()) {
                    FilaDetalle(etiqueta = "Devolución", valor = item.politicaDevolucion)
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (item.esFavorito) {
            OutlinedButton(
                onClick = onAlternarFavorito,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Quitar de favoritos")
            }
        } else {
            Button(
                onClick = onAlternarFavorito,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Agregar a favoritos")
            }
        }
    }
}

@Composable
private fun FilaDetalle(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
