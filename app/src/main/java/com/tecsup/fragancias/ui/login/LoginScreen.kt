package com.tecsup.fragancias.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.fragancias.R
import com.tecsup.fragancias.ui.UiState
import com.tecsup.fragancias.ui.theme.BlancoPuro
import com.tecsup.fragancias.ui.theme.BordeOscuro
import com.tecsup.fragancias.ui.theme.DoradoChampan
import com.tecsup.fragancias.ui.theme.ErrorOscuro
import com.tecsup.fragancias.ui.theme.NegroObsidiana
import com.tecsup.fragancias.ui.theme.SuperficieOscura
import com.tecsup.fragancias.ui.theme.TarjetaOscura
import com.tecsup.fragancias.ui.theme.TextoSecundarioOscuro

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onIrARegistro: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val correo by viewModel.correo.collectAsStateWithLifecycle()
    val clave by viewModel.clave.collectAsStateWithLifecycle()
    val cargando = estado is UiState.Cargando
    var mostrarClave by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NegroObsidiana)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            HeroCollageMarcas()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 12.dp, bottom = 28.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = correo,
                    onValueChange = viewModel::onCorreoChange,
                    label = {
                        Text(
                            text = "Correo",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    enabled = !cargando,
                    shape = RoundedCornerShape(16.dp),
                    colors = camposAuthOscuros(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = clave,
                    onValueChange = viewModel::onClaveChange,
                    label = {
                        Text(
                            text = "Contraseña",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    enabled = !cargando,
                    shape = RoundedCornerShape(16.dp),
                    colors = camposAuthOscuros(),
                    visualTransformation = if (mostrarClave) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(onClick = { mostrarClave = !mostrarClave }) {
                            Icon(
                                imageVector = if (mostrarClave) {
                                    Icons.Outlined.VisibilityOff
                                } else {
                                    Icons.Outlined.Visibility
                                },
                                contentDescription = if (mostrarClave) {
                                    "Ocultar contraseña"
                                } else {
                                    "Mostrar contraseña"
                                }
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { if (!cargando) viewModel.iniciarSesion() }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (estado is UiState.Error) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = (estado as UiState.Error).mensaje,
                        color = ErrorOscuro,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = viewModel::iniciarSesion,
                    enabled = !cargando,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlancoPuro,
                        contentColor = NegroObsidiana,
                        disabledContainerColor = BlancoPuro.copy(alpha = 0.55f),
                        disabledContentColor = NegroObsidiana
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    if (cargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = NegroObsidiana
                        )
                    } else {
                        Text(
                            text = "Iniciar sesión",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "¿No tienes cuenta?",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundarioOscuro,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onIrARegistro,
                    enabled = !cargando,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, BordeOscuro),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = TarjetaOscura,
                        contentColor = BlancoPuro
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Crear cuenta",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroCollageMarcas() {
    val inversionOscura = ColorMatrix(
        floatArrayOf(
            -1f, 0f, 0f, 0f, 240f,
            0f, -1f, 0f, 0f, 240f,
            0f, 0f, -1f, 0f, 240f,
            0f, 0f, 0f, 1f, 0f
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(265.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.collage_marcas),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.28f,
            colorFilter = ColorFilter.colorMatrix(inversionOscura),
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            NegroObsidiana.copy(alpha = 0.35f),
                            NegroObsidiana.copy(alpha = 0.72f),
                            NegroObsidiana
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_fragancias_rocha),
                contentDescription = "Logo Fragancias Rocha",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(76.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "FRAGANCIAS ROCHA",
                style = MaterialTheme.typography.titleLarge,
                color = BlancoPuro,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.5.sp,
                maxLines = 1,
                softWrap = false
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Esencias de Alta Gama",
                style = MaterialTheme.typography.titleSmall,
                color = DoradoChampan,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Catálogo exclusivo de perfumes y favoritos",
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundarioOscuro,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
internal fun camposAuthOscuros() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = BlancoPuro,
    unfocusedTextColor = BlancoPuro,
    disabledTextColor = TextoSecundarioOscuro,
    focusedContainerColor = TarjetaOscura,
    unfocusedContainerColor = SuperficieOscura,
    disabledContainerColor = SuperficieOscura,
    focusedBorderColor = DoradoChampan,
    unfocusedBorderColor = BordeOscuro,
    focusedLabelColor = DoradoChampan,
    unfocusedLabelColor = TextoSecundarioOscuro,
    focusedLeadingIconColor = DoradoChampan,
    unfocusedLeadingIconColor = TextoSecundarioOscuro,
    focusedTrailingIconColor = BlancoPuro,
    unfocusedTrailingIconColor = TextoSecundarioOscuro,
    focusedPlaceholderColor = TextoSecundarioOscuro,
    unfocusedPlaceholderColor = TextoSecundarioOscuro,
    cursorColor = DoradoChampan
)
