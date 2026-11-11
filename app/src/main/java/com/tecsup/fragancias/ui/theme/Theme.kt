package com.tecsup.fragancias.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BlancoPuro,
    onPrimary = NegroObsidiana,
    secondary = DoradoChampan,
    onSecondary = NegroObsidiana,
    secondaryContainer = TarjetaOscura,
    onSecondaryContainer = DoradoChampan,
    tertiary = DoradoChampan,
    background = NegroObsidiana,
    onBackground = BlancoPuro,
    surface = SuperficieOscura,
    onSurface = BlancoPuro,
    surfaceVariant = TarjetaOscura,
    onSurfaceVariant = TextoSecundarioOscuro,
    outline = BordeOscuro,
    error = ErrorOscuro
)

private val LightColorScheme = lightColorScheme(
    primary = NegroObsidiana,
    onPrimary = BlancoPuro,
    secondary = DoradoOscuro,
    onSecondary = BlancoPuro,
    secondaryContainer = CremaTarjeta,
    onSecondaryContainer = NegroObsidiana,
    tertiary = DoradoOscuro,
    background = MarfilClaro,
    onBackground = NegroObsidiana,
    surface = MarfilClaro,
    onSurface = NegroObsidiana,
    surfaceVariant = CremaTarjeta,
    onSurfaceVariant = TextoSecundarioClaro,
    outline = BordeClaro,
    error = ErrorClaro
)

@Composable
fun FraganciasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
