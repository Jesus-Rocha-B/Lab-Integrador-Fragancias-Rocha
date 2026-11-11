package com.tecsup.fragancias.ui.login

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
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecsup.fragancias.domain.model.Rol
import com.tecsup.fragancias.domain.model.Sesion
import com.tecsup.fragancias.ui.components.BarraInferior
import com.tecsup.fragancias.ui.components.FondoCollageMarcas
import com.tecsup.fragancias.ui.theme.BlancoPuro
import com.tecsup.fragancias.ui.theme.BordeOscuro
import com.tecsup.fragancias.ui.theme.DoradoChampan
import com.tecsup.fragancias.ui.theme.NegroObsidiana
import com.tecsup.fragancias.ui.theme.TarjetaOscura
import com.tecsup.fragancias.ui.theme.TextoSecundarioOscuro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    sesion: Sesion,
    claveSesion: String?,
    onNavegarTab: (String) -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val esAdmin = sesion.rol == Rol.ADMIN
    var mostrarClave by rememberSaveable { mutableStateOf(false) }

    val nombreMostrado = sesion.nombre.ifBlank {
        sesion.correo.substringBefore("@").ifBlank { "Mi cuenta" }
    }
    val textoClave = if (mostrarClave) {
        claveSesion ?: "Protegida por Firebase Auth"
    } else {
        "••••••••••••"
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Perfil",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }
            )
        },
        bottomBar = {
            BarraInferior(
                rutaActual = "perfil",
                esAdmin = esAdmin,
                onNavegar = onNavegarTab
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp),
                contentAlignment = Alignment.Center
            ) {
                FondoCollageMarcas(
                    colorFondoInferior = NegroObsidiana.copy(alpha = 0.92f),
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        color = TarjetaOscura.copy(alpha = 0.95f),
                        border = BorderStroke(1.5.dp, DoradoChampan.copy(alpha = 0.85f)),
                        shadowElevation = 6.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = "Perfil de usuario",
                                tint = DoradoChampan,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = nombreMostrado,
                        style = MaterialTheme.typography.titleLarge,
                        color = BlancoPuro,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
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
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Datos de la cuenta",
                            style = MaterialTheme.typography.titleMedium,
                            color = BlancoPuro,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )

                        FilaDatoPerfil(
                            icono = Icons.Outlined.Email,
                            etiqueta = "Correo",
                            valor = sesion.correo
                        )
                        HorizontalDivider(color = BordeOscuro)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Lock,
                                    contentDescription = null,
                                    tint = DoradoChampan
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Contraseña",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextoSecundarioOscuro,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = textoClave,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = BlancoPuro,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            IconButton(onClick = { mostrarClave = !mostrarClave }) {
                                Icon(
                                    imageVector = if (mostrarClave) {
                                        Icons.Outlined.VisibilityOff
                                    } else {
                                        Icons.Outlined.Visibility
                                    },
                                    tint = BlancoPuro,
                                    contentDescription = if (mostrarClave) {
                                        "Ocultar contraseña"
                                    } else {
                                        "Mostrar contraseña"
                                    }
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = onCerrarSesion,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Logout,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Cerrar sesión",
                        maxLines = 1,
                        softWrap = false,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun FilaDatoPerfil(
    icono: ImageVector,
    etiqueta: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = DoradoChampan
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.labelSmall,
                color = TextoSecundarioOscuro,
                maxLines = 1
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyLarge,
                color = BlancoPuro,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
