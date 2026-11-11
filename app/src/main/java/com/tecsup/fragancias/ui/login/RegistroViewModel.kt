package com.tecsup.fragancias.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.fragancias.AppContainer
import com.tecsup.fragancias.data.auth.AuthRepository
import com.tecsup.fragancias.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistroViewModel(
    private val auth: AuthRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Boolean>>(UiState.Exito(false))
    val estado: StateFlow<UiState<Boolean>> = _estado.asStateFlow()

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _correo = MutableStateFlow("")
    val correo: StateFlow<String> = _correo.asStateFlow()

    private val _clave = MutableStateFlow("")
    val clave: StateFlow<String> = _clave.asStateFlow()

    private val _confirmarClave = MutableStateFlow("")
    val confirmarClave: StateFlow<String> = _confirmarClave.asStateFlow()

    fun onNombreChange(valor: String) {
        _nombre.value = valor
        limpiarError()
    }

    fun onCorreoChange(valor: String) {
        _correo.value = valor
        limpiarError()
    }

    fun onClaveChange(valor: String) {
        _clave.value = valor
        limpiarError()
    }

    fun onConfirmarClaveChange(valor: String) {
        _confirmarClave.value = valor
        limpiarError()
    }

    fun registrar() {
        val nombreLimpio = _nombre.value.trim()
        val correoLimpio = _correo.value.trim()
        val claveActual = _clave.value
        val confirmacion = _confirmarClave.value

        when {
            nombreLimpio.isBlank() || correoLimpio.isBlank() || claveActual.isBlank() || confirmacion.isBlank() -> {
                _estado.value = UiState.Error("Completa todos los campos")
                return
            }
            !correoLimpio.contains("@") || !correoLimpio.contains(".") -> {
                _estado.value = UiState.Error("Ingresa un correo electrónico válido")
                return
            }
            claveActual.length < 6 -> {
                _estado.value = UiState.Error("La contraseña debe tener al menos 6 caracteres")
                return
            }
            claveActual != confirmacion -> {
                _estado.value = UiState.Error("Las contraseñas no coinciden")
                return
            }
        }

        viewModelScope.launch {
            _estado.value = UiState.Cargando
            auth.registrar(nombreLimpio, correoLimpio, claveActual)
                .onSuccess {
                    _clave.value = ""
                    _confirmarClave.value = ""
                    _estado.value = UiState.Exito(true)
                }
                .onFailure { e ->
                    _estado.value = UiState.Error(
                        e.message ?: "No se pudo crear la cuenta"
                    )
                }
        }
    }

    fun limpiar() {
        _nombre.value = ""
        _correo.value = ""
        _clave.value = ""
        _confirmarClave.value = ""
        _estado.value = UiState.Exito(false)
    }

    private fun limpiarError() {
        if (_estado.value is UiState.Error) {
            _estado.value = UiState.Exito(false)
        }
    }

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RegistroViewModel(
                        auth = container.authRepository
                    ) as T
                }
            }
    }
}
