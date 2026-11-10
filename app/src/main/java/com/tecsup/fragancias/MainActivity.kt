package com.tecsup.fragancias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.fragancias.ui.lista.ListaScreen
import com.tecsup.fragancias.ui.lista.ListaViewModel
import com.tecsup.fragancias.ui.theme.FraganciasTheme

class MainActivity : ComponentActivity() {

    private val appContainer by lazy { AppContainer() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FraganciasTheme {
                val listaViewModel: ListaViewModel = viewModel(
                    factory = ListaViewModel.factory(appContainer)
                )
                ListaScreen(viewModel = listaViewModel)
            }
        }
    }
}
