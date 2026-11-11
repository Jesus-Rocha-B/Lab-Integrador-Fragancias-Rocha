package com.tecsup.fragancias.ui.login

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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.fragancias.R
import com.tecsup.fragancias.ui.UiState
import com.tecsup.fragancias.ui.theme.BlancoPuro
import com.tecsup.fragancias.ui.theme.DoradoChampan
import com.tecsup.fragancias.ui.theme.ErrorOscuro
import com.tecsup.fragancias.ui.theme.NegroObsidiana
import com.tecsup.fragancias.ui.theme.TextoSecundarioOscuro

@Composable
fun RegistroScreen(
    viewModel: RegistroViewModel,
    onVolver: () -> Unit,
    onRegistroExitoso: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val nombre by viewModel.nombre.collectAsStateWithLifecycle()
    val correo by viewModel.correo.collectAsStateWithLifecycle()
    val clave by viewModel.clave.collectAsStateWithLifecycle()
    val confirmarClave by viewModel.confirmarClave.collectAsStateWithLifecycle()
    val cargando = estado is UiState.Cargando
    val cuentaCreada = (estado as? UiState.Exito)?.datos == true
    var mostrarClave by rememberSaveable { mutableStateOf(false) }

    if (cuentaCreada) {
        AlertDialog(
            onDismissRequest = {
                val correoRegistrado = correo
                viewModel.limpiar()
                onRegistroExitoso(correoRegistrado)
            },
            title = { Text(text = "Cuenta creada") },
            text = {
                Text(
                    text = "Tu cuenta se creó de manera exitosa. Ahora inicia sesión con tu correo y contraseña."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val correoRegistrado = correo
                        viewModel.limpiar()
                        onRegistroExitoso(correoRegistrado)
                    }
                ) {
                    Text(text = "Ir al login", maxLines = 1, softWrap = false)
                }
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NegroObsidiana)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = onVolver,
                    enabled = !cargando
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Volver",
                        tint = BlancoPuro
                    )
                }
            }

            Image(
                painter = painterResource(id = R.drawable.logo_fragancias_rocha),
                contentDescription = "Logo Fragancias Rocha",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineSmall,
                color = BlancoPuro,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Completa tus datos de usuario",
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundarioOscuro,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(22.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = viewModel::onNombreChange,
                label = {
                    Text(
                        text = "Nombre completo",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null
                    )
                },
                singleLine = true,
                enabled = !cargando,
                shape = RoundedCornerShape(16.dp),
                colors = camposAuthOscuros(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

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
                placeholder = {
                    Text(
                        text = "Mínimo 6 caracteres",
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
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmarClave,
                onValueChange = viewModel::onConfirmarClaveChange,
                label = {
                    Text(
                        text = "Confirmar contraseña",
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
                    onDone = { if (!cargando) viewModel.registrar() }
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
                onClick = viewModel::registrar,
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
                        text = "Crear cuenta",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes cuenta?",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundarioOscuro,
                    maxLines = 1
                )
                TextButton(
                    onClick = onVolver,
                    enabled = !cargando
                ) {
                    Text(
                        text = "Inicia sesión",
                        color = DoradoChampan,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}
