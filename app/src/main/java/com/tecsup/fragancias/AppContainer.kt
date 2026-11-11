package com.tecsup.fragancias

import android.content.Context
import com.tecsup.fragancias.data.auth.AuthRepository
import com.tecsup.fragancias.data.auth.FirebaseAuthRepository
import com.tecsup.fragancias.data.local.AppDatabase
import com.tecsup.fragancias.data.remote.RetrofitClient
import com.tecsup.fragancias.data.repository.ItemRepository
import com.tecsup.fragancias.data.repository.ItemRepositoryImpl
import com.tecsup.fragancias.notification.Notificador
import com.tecsup.fragancias.notification.NotificadorAndroid

class AppContainer(context: Context) {
    private val appContext = context.applicationContext
    private val apiService = RetrofitClient.apiService
    private val database = AppDatabase.obtener(appContext)

    val authRepository: AuthRepository by lazy {
        FirebaseAuthRepository()
    }

    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(
            api = apiService,
            dao = database.itemDao()
        )
    }

    val notificador: Notificador by lazy {
        NotificadorAndroid(appContext)
    }
}
