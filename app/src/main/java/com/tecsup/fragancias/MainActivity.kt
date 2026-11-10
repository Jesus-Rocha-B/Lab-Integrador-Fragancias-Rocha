package com.tecsup.fragancias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecsup.fragancias.ui.navigation.AppNav
import com.tecsup.fragancias.ui.theme.FraganciasTheme

class MainActivity : ComponentActivity() {

    private val appContainer by lazy { AppContainer() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FraganciasTheme {
                AppNav(appContainer = appContainer)
            }
        }
    }
}
