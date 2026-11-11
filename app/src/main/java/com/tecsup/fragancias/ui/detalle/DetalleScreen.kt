package com.tecsup.fragancias.ui.detalle

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.StarHalf
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.tecsup.fragancias.domain.model.Item
import com.tecsup.fragancias.ui.UiState
import com.tecsup.fragancias.ui.components.Cargando
import com.tecsup.fragancias.ui.components.ErrorView
import com.tecsup.fragancias.ui.theme.DoradoChampan
import java.util.Locale

private val RojoFavoritoDetalle = Color(0xFFD92D43)
private val EstrellaDorada = Color(0xFFF5A623)

@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Scaffold(modifier = modifier.fillMaxSize()) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = estado,
                transitionSpec = {
                    fadeIn(tween(280)) togetherWith fadeOut(tween(200))
                },
                label = "DetalleEstado"
            ) { actual ->
                when (actual) {
                    is UiState.Cargando -> Box(modifier = Modifier.fillMaxSize()) {
                        Cargando()
                        IconButton(
                            onClick = onVolver,
                            modifier = Modifier
                                .statusBarsPadding()
                                .padding(8.dp)
                                .align(Alignment.TopStart)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    }
                    is UiState.Error -> Box(modifier = Modifier.fillMaxSize()) {
                        ErrorView(
                            mensaje = actual.mensaje,
                            onReintentar = viewModel::cargarDetalle
                        )
                        IconButton(
                            onClick = onVolver,
                            modifier = Modifier
                                .statusBarsPadding()
                                .padding(8.dp)
                                .align(Alignment.TopStart)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    }
                    is UiState.Exito -> DetalleContenido(
                        item = actual.datos,
                        onVolver = onVolver,
                        onAlternarFavorito = viewModel::alternarFavorito
                    )
                }
            }
        }
    }
}

@Composable
private fun DetalleContenido(
    item: Item,
    onVolver: () -> Unit,
    onAlternarFavorito: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(item.id) {
        visible = true
    }

    val escalaCorazon by animateFloatAsState(
        targetValue = if (item.esFavorito) 1.22f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "EscalaFavorito"
    )
    val colorFavorito by animateColorAsState(
        targetValue = if (item.esFavorito) RojoFavoritoDetalle else MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(220),
        label = "ColorFavorito"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = item.imagenUrl,
                contentDescription = item.titulo,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 36.dp, vertical = 20.dp),
                loading = {
                    Icon(
                        imageVector = Icons.Outlined.Spa,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(64.dp)
                    )
                },
                error = {
                    Icon(
                        imageVector = Icons.Outlined.Spa,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(64.dp)
                    )
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onVolver),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onAlternarFavorito),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (item.esFavorito) {
                                Icons.Rounded.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            },
                            contentDescription = if (item.esFavorito) {
                                "Quitar de favoritos"
                            } else {
                                "Agregar a favoritos"
                            },
                            tint = colorFavorito,
                            modifier = Modifier
                                .size(22.dp)
                                .scale(escalaCorazon)
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(380)) + slideInVertically(tween(380)) { it / 8 }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = item.titulo,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    if (item.marca.isNotBlank()) {
                        Text(
                            text = item.marca,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                FilaEstrellas(rating = item.rating)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = String.format(Locale.US, "$%.2f", item.precio),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    if (item.descuento > 0 && item.descuento < 100) {
                        val precioOriginal = item.precio / (1.0 - item.descuento / 100.0)
                        Text(
                            text = String.format(Locale.US, "$%.2f", precioOriginal),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = TextDecoration.LineThrough
                        )
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = DoradoChampan.copy(alpha = 0.22f)
                        ) {
                            Text(
                                text = String.format(Locale.US, "-%.0f%%", item.descuento),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Text(
                    text = item.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FilaDetalle(
                            icono = Icons.Outlined.Inventory2,
                            etiqueta = "Stock disponible",
                            valor = "${item.stock} unidades"
                        )
                        if (item.categoria.isNotBlank()) {
                            HorizontalDivider()
                            FilaDetalle(
                                icono = Icons.Outlined.Category,
                                etiqueta = "Categoría",
                                valor = item.categoria
                            )
                        }
                        if (item.disponibilidad.isNotBlank()) {
                            HorizontalDivider()
                            FilaDetalle(
                                icono = Icons.Outlined.Spa,
                                etiqueta = "Estado",
                                valor = item.disponibilidad
                            )
                        }
                        if (item.garantia.isNotBlank()) {
                            HorizontalDivider()
                            FilaDetalle(
                                icono = Icons.Outlined.VerifiedUser,
                                etiqueta = "Garantía",
                                valor = item.garantia
                            )
                        }
                        if (item.envio.isNotBlank()) {
                            HorizontalDivider()
                            FilaDetalle(
                                icono = Icons.Outlined.LocalShipping,
                                etiqueta = "Envío",
                                valor = item.envio
                            )
                        }
                        if (item.politicaDevolucion.isNotBlank()) {
                            HorizontalDivider()
                            FilaDetalle(
                                icono = Icons.Outlined.Autorenew,
                                etiqueta = "Devolución",
                                valor = item.politicaDevolucion
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaEstrellas(rating: Double) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        for (i in 1..5) {
            val icono = when {
                rating >= i -> Icons.Rounded.Star
                rating >= i - 0.5 -> Icons.AutoMirrored.Rounded.StarHalf
                else -> Icons.Rounded.StarBorder
            }
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = EstrellaDorada,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = String.format(Locale.US, "%.2f", rating),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun FilaDetalle(
    icono: ImageVector,
    etiqueta: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
