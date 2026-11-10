package com.tecsup.fragancias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecsup.fragancias.ui.lista.ListaScreen
import com.tecsup.fragancias.ui.theme.FraganciasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FraganciasTheme {
                ListaScreen()
            }
        }
    }
}
