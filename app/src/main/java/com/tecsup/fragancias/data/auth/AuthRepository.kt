package com.tecsup.fragancias.data.auth

import com.tecsup.fragancias.domain.model.Sesion

interface AuthRepository {
    suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion>
    suspend fun registrar(nombre: String, correo: String, clave: String): Result<Unit>
    suspend fun sesionActual(): Sesion?
    fun cerrarSesion()
}
