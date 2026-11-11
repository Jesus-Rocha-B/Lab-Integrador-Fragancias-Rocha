package com.tecsup.fragancias

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.tecsup.fragancias.data.auth.AuthRepository
import com.tecsup.fragancias.data.auth.FirebaseAuthRepository
import com.tecsup.fragancias.data.local.AppDatabase
import com.tecsup.fragancias.data.remote.RetrofitClient
import com.tecsup.fragancias.data.repository.ItemRepository
import com.tecsup.fragancias.data.repository.ItemRepositoryImpl
import com.tecsup.fragancias.notification.Notificador
import com.tecsup.fragancias.notification.NotificadorAndroid
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class AppContainer(context: Context) {
    private val appContext = context.applicationContext
    private val apiService = RetrofitClient.apiService
    private val database = AppDatabase.obtener(appContext)
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val uidFlow: Flow<String> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.uid.orEmpty())
        }
        firebaseAuth.addAuthStateListener(listener)
        trySend(firebaseAuth.currentUser?.uid.orEmpty())
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }.distinctUntilChanged()

    val authRepository: AuthRepository by lazy {
        FirebaseAuthRepository(auth = firebaseAuth)
    }

    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(
            api = apiService,
            dao = database.itemDao(),
            uidFlow = uidFlow,
            obtenerUid = { firebaseAuth.currentUser?.uid.orEmpty() }
        )
    }

    val notificador: Notificador by lazy {
        NotificadorAndroid(appContext)
    }
}
