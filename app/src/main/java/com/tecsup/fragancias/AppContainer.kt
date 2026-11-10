package com.tecsup.fragancias

import com.tecsup.fragancias.data.remote.RetrofitClient
import com.tecsup.fragancias.data.repository.ItemRepository
import com.tecsup.fragancias.data.repository.ItemRepositoryImpl
import com.tecsup.fragancias.notification.Notificador
import com.tecsup.fragancias.notification.NotificadorAndroid

class AppContainer {
    private val apiService = RetrofitClient.apiService

    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(api = apiService)
    }

    val notificador: Notificador by lazy {
        NotificadorAndroid()
    }
}
