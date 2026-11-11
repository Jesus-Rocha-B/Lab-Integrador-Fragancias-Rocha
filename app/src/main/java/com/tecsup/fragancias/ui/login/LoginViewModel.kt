package com.tecsup.fragancias.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.tecsup.fragancias.AppContainer
import com.tecsup.fragancias.data.auth.AuthRepository
import com.tecsup.fragancias.domain.model.Sesion
import com.tecsup.fragancias.ui.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel(
    private val auth: AuthRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Sesion?>>(UiState.Cargando)
    val estado: StateFlow<UiState<Sesion?>> = _estado.asStateFlow()

    private val _correo = MutableStateFlow("")
    val correo: StateFlow<String> = _correo.asStateFlow()

    private val _clave = MutableStateFlow("")
    val clave: StateFlow<String> = _clave.asStateFlow()

    private val _verificando = MutableStateFlow(true)
    val verificando: StateFlow<Boolean> = _verificando.asStateFlow()

    private val _splashListo = MutableStateFlow(false)
    val splashListo: StateFlow<Boolean> = _splashListo.asStateFlow()

    init {
        viewModelScope.launch {
            val sesion = auth.sesionActual()
            _estado.value = UiState.Exito(sesion)
            _verificando.value = false
        }
        viewModelScope.launch {
            delay(1400)
            _splashListo.value = true
        }
    }

    fun onCorreoChange(valor: String) {
        _correo.value = valor
        if (_estado.value is UiState.Error) {
            _estado.value = UiState.Exito(null)
        }
    }

    fun onClaveChange(valor: String) {
        _clave.value = valor
        if (_estado.value is UiState.Error) {
            _estado.value = UiState.Exito(null)
        }
    }

    fun prepararCorreo(correoNuevo: String) {
        _correo.value = correoNuevo.trim()
        _clave.value = ""
        _estado.value = UiState.Exito(null)
    }

    fun iniciarSesion() {
        val correoLimpio = _correo.value.trim()
        val claveActual = _clave.value
        if (correoLimpio.isBlank() || claveActual.isBlank()) {
            _estado.value = UiState.Error("Ingresa correo y contraseña")
            return
        }

        viewModelScope.launch {
            _estado.value = UiState.Cargando
            auth.iniciarSesion(correoLimpio, claveActual)
                .onSuccess { sesion ->
                    _clave.value = ""
                    _estado.value = UiState.Exito(sesion)
                }
                .onFailure { e ->
                    _estado.value = UiState.Error(mensajeError(e))
                }
        }
    }

    fun cerrarSesion() {
        auth.cerrarSesion()
        _correo.value = ""
        _clave.value = ""
        _estado.value = UiState.Exito(null)
    }

    private fun mensajeError(e: Throwable): String = when (e) {
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> "Correo o contraseña incorrectos"
        is FirebaseNetworkException,
        is IOException -> "No hay conexión a internet"
        else -> "No se pudo iniciar sesión. Verifica tus credenciales"
    }

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LoginViewModel(
                        auth = container.authRepository
                    ) as T
                }
            }
    }
}
