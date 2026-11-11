package com.tecsup.fragancias.ui.login

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.fragancias.R
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    val escala = remember { Animatable(0.72f) }
    val opacidadLogo = remember { Animatable(0f) }
    val opacidadTexto = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            opacidadLogo.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 550)
            )
        }
        launch {
            escala.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        launch {
            opacidadTexto.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 650, delayMillis = 240)
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF050506)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val ondaSuperior = Path().apply {
                moveTo(0f, h * 0.12f)
                cubicTo(w * 0.35f, h * 0.02f, w * 0.65f, h * 0.24f, w, h * 0.14f)
                lineTo(w, h * 0.42f)
                cubicTo(w * 0.65f, h * 0.50f, w * 0.3f, h * 0.30f, 0f, h * 0.38f)
                close()
            }
            drawPath(
                path = ondaSuperior,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF1A1A1E), Color(0xFF070709)),
                    start = Offset(0f, 0f),
                    end = Offset(w, h * 0.42f)
                )
            )

            val ondaMedia = Path().apply {
                moveTo(0f, h * 0.36f)
                cubicTo(w * 0.4f, h * 0.52f, w * 0.7f, h * 0.30f, w, h * 0.48f)
                lineTo(w, h * 0.78f)
                cubicTo(w * 0.65f, h * 0.62f, w * 0.32f, h * 0.80f, 0f, h * 0.66f)
                close()
            }
            drawPath(
                path = ondaMedia,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF16161A), Color(0xFF08080A)),
                    start = Offset(0f, h * 0.36f),
                    end = Offset(w, h * 0.78f)
                )
            )

            val ondaInferior = Path().apply {
                moveTo(0f, h * 0.74f)
                cubicTo(w * 0.3f, h * 0.86f, w * 0.72f, h * 0.66f, w, h * 0.80f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(
                path = ondaInferior,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF131317), Color(0xFF040405)),
                    start = Offset(0f, h * 0.74f),
                    end = Offset(w, h)
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_fragancias_rocha),
                contentDescription = "Logo Fragancias Rocha",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(136.dp)
                    .scale(escala.value)
                    .alpha(opacidadLogo.value)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "FRAGANCIAS ROCHA",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                letterSpacing = 4.sp,
                color = Color.White,
                maxLines = 1,
                modifier = Modifier.alpha(opacidadTexto.value)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Aroma que deja huella",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light,
                fontSize = 13.sp,
                letterSpacing = 0.6.sp,
                color = Color(0xFFBDBDC4),
                maxLines = 1,
                modifier = Modifier.alpha(opacidadTexto.value)
            )
        }
    }
}
