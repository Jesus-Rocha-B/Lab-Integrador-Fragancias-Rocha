package com.tecsup.fragancias.data.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.fragancias.domain.model.Rol
import com.tecsup.fragancias.domain.model.Sesion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> = try {
        val correoLimpio = correo.trim()
        val uid = auth.signInWithEmailAndPassword(correoLimpio, clave).await().user!!.uid
        Result.success(leerSesion(uid, correoLimpio))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun registrar(nombre: String, correo: String, clave: String): Result<Unit> = try {
        val nombreLimpio = nombre.trim()
        val correoLimpio = correo.trim()
        val resultado = auth.createUserWithEmailAndPassword(correoLimpio, clave).await()
        val uid = resultado.user?.uid
        if (uid != null) {
            try {
                db.collection("usuarios").document(uid).set(
                    mapOf(
                        "rol" to "usuario",
                        "nombre" to nombreLimpio
                    )
                ).await()
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
            }
        }
        auth.signOut()
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        val mensaje = when (e) {
            is FirebaseAuthUserCollisionException -> "Ya existe una cuenta registrada con ese correo"
            is FirebaseAuthWeakPasswordException -> "La contraseña debe tener al menos 6 caracteres"
            is FirebaseAuthInvalidCredentialsException -> "El formato del correo no es válido"
            is FirebaseNetworkException -> "No hay conexión a internet"
            else -> "No se pudo crear la cuenta. Intenta nuevamente"
        }
        Result.failure(IllegalStateException(mensaje, e))
    }

    override suspend fun sesionActual(): Sesion? {
        val usuario = auth.currentUser ?: return null
        return try {
            leerSesion(usuario.uid, usuario.email.orEmpty())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Sesion(usuario.uid, usuario.email.orEmpty(), Rol.USUARIO)
        }
    }

    override fun cerrarSesion() = auth.signOut()

    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        val doc = db.collection("usuarios").document(uid).get().await()
        val rol = if (doc.getString("rol") == "admin") Rol.ADMIN else Rol.USUARIO
        val nombre = doc.getString("nombre").orEmpty()
        return Sesion(uid, correo, rol, nombre)
    }
}
