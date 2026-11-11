package com.tecsup.fragancias.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.tecsup.fragancias.R
import com.tecsup.fragancias.ui.theme.NegroObsidiana

@Composable
fun FondoCollageMarcas(
    colorFondoInferior: Color = NegroObsidiana,
    modifier: Modifier = Modifier
) {
    val inversionOscura = ColorMatrix(
        floatArrayOf(
            -1f, 0f, 0f, 0f, 240f,
            0f, -1f, 0f, 0f, 240f,
            0f, 0f, -1f, 0f, 240f,
            0f, 0f, 0f, 1f, 0f
        )
    )

    Box(modifier = modifier.background(NegroObsidiana)) {
        Image(
            painter = painterResource(id = R.drawable.collage_marcas),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.28f,
            colorFilter = ColorFilter.colorMatrix(inversionOscura),
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            NegroObsidiana.copy(alpha = 0.35f),
                            NegroObsidiana.copy(alpha = 0.70f),
                            colorFondoInferior
                        )
                    )
                )
        )
    }
}
