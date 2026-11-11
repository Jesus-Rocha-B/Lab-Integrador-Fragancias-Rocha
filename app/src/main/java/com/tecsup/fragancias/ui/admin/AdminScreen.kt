package com.tecsup.fragancias.ui.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.fragancias.ui.UiState
import com.tecsup.fragancias.ui.components.BarraInferior
import com.tecsup.fragancias.ui.components.Cargando
import com.tecsup.fragancias.ui.components.ErrorView
import com.tecsup.fragancias.ui.theme.BlancoPuro
import com.tecsup.fragancias.ui.theme.BordeOscuro
import com.tecsup.fragancias.ui.theme.DoradoChampan
import com.tecsup.fragancias.ui.theme.ErrorOscuro
import com.tecsup.fragancias.ui.theme.NegroObsidiana
import com.tecsup.fragancias.ui.theme.TarjetaOscura
import com.tecsup.fragancias.ui.theme.TextoSecundarioOscuro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: AdminViewModel,
    correoUsuario: String,
    onNavegarTab: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Panel Admin",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }
            )
        },
        bottomBar = {
            BarraInferior(
                rutaActual = "admin",
                esAdmin = true,
                onNavegar = onNavegarTab
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
                    onReintentar = viewModel::cargarTotales
                )
                is UiState.Exito -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            border = BorderStroke(1.dp, BordeOscuro),
                            colors = CardDefaults.cardColors(
                                containerColor = TarjetaOscura,
                                contentColor = BlancoPuro
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Surface(
                                        modifier = Modifier.size(44.dp),
                                        shape = CircleShape,
                                        color = NegroObsidiana,
                                        border = BorderStroke(1.dp, DoradoChampan.copy(alpha = 0.7f))
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Rounded.AdminPanelSettings,
                                                contentDescription = null,
                                                tint = DoradoChampan,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = "Consola de catálogo",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = BlancoPuro,
                                                fontWeight = FontWeight.SemiBold,
                                                maxLines = 1
                                            )
                                            Surface(
                                                shape = RoundedCornerShape(50),
                                                color = DoradoChampan.copy(alpha = 0.16f),
                                                border = BorderStroke(1.dp, DoradoChampan.copy(alpha = 0.6f))
                                            ) {
                                                Text(
                                                    text = "ADMIN",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    letterSpacing = 1.sp,
                                                    color = DoradoChampan,
                                                    maxLines = 1,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        if (correoUsuario.isNotBlank()) {
                                            Text(
                                                text = correoUsuario,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = TextoSecundarioOscuro,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "Supervisa el estado de la base local Room y mantén actualizado el catálogo desde el servidor.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextoSecundarioOscuro
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TarjetaMetricaAdmin(
                                icono = Icons.Outlined.Inventory2,
                                valor = actual.datos.totalGuardado.toString(),
                                titulo = "En caché local",
                                subtitulo = "Listos sin conexión",
                                modifier = Modifier.weight(1f)
                            )
                            TarjetaMetricaAdmin(
                                icono = Icons.Rounded.Favorite,
                                valor = actual.datos.totalFavoritos.toString(),
                                titulo = "Tus favoritos",
                                subtitulo = "Solo este perfil",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        AnimatedVisibility(
                            visible = mensaje != null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            mensaje?.let { textoMensaje ->
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    color = TarjetaOscura,
                                    border = BorderStroke(1.dp, DoradoChampan.copy(alpha = 0.65f))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.CheckCircle,
                                            contentDescription = null,
                                            tint = DoradoChampan,
                                            modifier = Modifier.size(19.dp)
                                        )
                                        Text(
                                            text = textoMensaje,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = BlancoPuro
                                        )
                                    }
                                }
                            }
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            border = BorderStroke(1.dp, BordeOscuro),
                            colors = CardDefaults.cardColors(
                                containerColor = TarjetaOscura,
                                contentColor = BlancoPuro
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "Operaciones de sincronización",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = BlancoPuro,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text(
                                        text = "Descarga las fragancias más recientes de la API y actualiza el almacenamiento local.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextoSecundarioOscuro
                                    )
                                    Button(
                                        onClick = viewModel::sincronizar,
                                        shape = RoundedCornerShape(50),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = DoradoChampan,
                                            contentColor = NegroObsidiana
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Sync,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                        Text(
                                            text = "Sincronizar catálogo",
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }

                                HorizontalDivider(color = BordeOscuro)

                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text(
                                        text = "Vacía los productos en caché para forzar una carga limpia en la siguiente sincronización.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextoSecundarioOscuro
                                    )
                                    OutlinedButton(
                                        onClick = viewModel::limpiarCache,
                                        shape = RoundedCornerShape(50),
                                        border = BorderStroke(1.dp, ErrorOscuro.copy(alpha = 0.65f)),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = ErrorOscuro
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.DeleteSweep,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                        Text(
                                            text = "Limpiar caché local",
                                            fontWeight = FontWeight.Medium,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaMetricaAdmin(
    icono: ImageVector,
    valor: String,
    titulo: String,
    subtitulo: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, BordeOscuro),
        colors = CardDefaults.cardColors(
            containerColor = TarjetaOscura,
            contentColor = BlancoPuro
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = NegroObsidiana,
                border = BorderStroke(1.dp, DoradoChampan.copy(alpha = 0.55f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = DoradoChampan,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Text(
                text = valor,
                style = MaterialTheme.typography.headlineLarge,
                color = BlancoPuro,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.labelLarge,
                    color = BlancoPuro,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextoSecundarioOscuro,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
