package com.tecsup.fragancias.ui.login

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.fragancias.R
import com.tecsup.fragancias.ui.theme.BlancoPuro
import com.tecsup.fragancias.ui.theme.DoradoChampan
import com.tecsup.fragancias.ui.theme.NegroObsidiana
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    val escala = remember { Animatable(0.68f) }
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
                animationSpec = tween(durationMillis = 650, delayMillis = 250)
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        DoradoChampan.copy(alpha = 0.14f),
                        NegroObsidiana
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_fragancias_rocha),
                contentDescription = "Logo Fragancias Rocha",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(132.dp)
                    .scale(escala.value)
                    .alpha(opacidadLogo.value)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "FRAGANCIAS ROCHA",
                style = MaterialTheme.typography.titleMedium,
                color = BlancoPuro,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                maxLines = 1,
                modifier = Modifier.alpha(opacidadTexto.value)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Perfumería de Autor",
                style = MaterialTheme.typography.bodySmall,
                color = DoradoChampan,
                maxLines = 1,
                modifier = Modifier.alpha(opacidadTexto.value)
            )
        }
    }
}
