package com.tecsup.fragancias.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.fragancias.ui.theme.BordeOscuro
import com.tecsup.fragancias.ui.theme.DoradoChampan
import com.tecsup.fragancias.ui.theme.NegroObsidiana
import com.tecsup.fragancias.ui.theme.TextoSecundarioOscuro

private data class NavTabItem(
    val ruta: String,
    val iconoActivo: ImageVector,
    val iconoInactivo: ImageVector,
    val etiqueta: String
)

private val IconoPerfumeActivo: ImageVector by lazy {
    ImageVector.Builder(
        name = "PerfumeActivo",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color.Black),
            pathFillType = PathFillType.EvenOdd
        ) {
            moveTo(10.2f, 2f)
            lineTo(13.8f, 2f)
            quadTo(14.8f, 2f, 14.8f, 3f)
            lineTo(14.8f, 5.3f)
            lineTo(9.2f, 5.3f)
            lineTo(9.2f, 3f)
            quadTo(9.2f, 2f, 10.2f, 2f)
            close()

            moveTo(10.2f, 6.1f)
            lineTo(13.8f, 6.1f)
            lineTo(13.8f, 7.7f)
            lineTo(10.2f, 7.7f)
            close()

            moveTo(7.6f, 8.4f)
            lineTo(16.4f, 8.4f)
            quadTo(19f, 8.4f, 19f, 11f)
            lineTo(19f, 18.9f)
            quadTo(19f, 21.5f, 16.4f, 21.5f)
            lineTo(7.6f, 21.5f)
            quadTo(5f, 21.5f, 5f, 18.9f)
            lineTo(5f, 11f)
            quadTo(5f, 8.4f, 7.6f, 8.4f)
            close()

            moveTo(9.2f, 12.2f)
            lineTo(14.8f, 12.2f)
            quadTo(15.4f, 12.2f, 15.4f, 12.8f)
            lineTo(15.4f, 17f)
            quadTo(15.4f, 17.6f, 14.8f, 17.6f)
            lineTo(9.2f, 17.6f)
            quadTo(8.6f, 17.6f, 8.6f, 17f)
            lineTo(8.6f, 12.8f)
            quadTo(8.6f, 12.2f, 9.2f, 12.2f)
            close()
        }
    }.build()
}

private val IconoPerfumeInactivo: ImageVector by lazy {
    ImageVector.Builder(
        name = "PerfumeInactivo",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.85f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(9.2f, 5.5f)
            lineTo(9.2f, 3.2f)
            quadTo(9.2f, 2.2f, 10.2f, 2.2f)
            lineTo(13.8f, 2.2f)
            quadTo(14.8f, 2.2f, 14.8f, 3.2f)
            lineTo(14.8f, 5.5f)
            close()

            moveTo(10.4f, 5.5f)
            lineTo(10.4f, 8.3f)
            moveTo(13.6f, 5.5f)
            lineTo(13.6f, 8.3f)

            moveTo(7.7f, 8.3f)
            lineTo(16.3f, 8.3f)
            quadTo(18.8f, 8.3f, 18.8f, 10.8f)
            lineTo(18.8f, 18.8f)
            quadTo(18.8f, 21.3f, 16.3f, 21.3f)
            lineTo(7.7f, 21.3f)
            quadTo(5.2f, 21.3f, 5.2f, 18.8f)
            lineTo(5.2f, 10.8f)
            quadTo(5.2f, 8.3f, 7.7f, 8.3f)
            close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.45f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(8.8f, 12.2f)
            lineTo(15.2f, 12.2f)
            lineTo(15.2f, 17.4f)
            lineTo(8.8f, 17.4f)
            close()
        }
    }.build()
}

@Composable
fun BarraInferior(
    rutaActual: String,
    esAdmin: Boolean,
    onNavegar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = remember(esAdmin) {
        buildList {
            add(NavTabItem("lista", IconoPerfumeActivo, IconoPerfumeInactivo, "Catálogo"))
            add(NavTabItem("favoritos", Icons.Rounded.Favorite, Icons.Outlined.FavoriteBorder, "Favoritos"))
            if (esAdmin) {
                add(NavTabItem("admin", Icons.Rounded.AdminPanelSettings, Icons.Outlined.AdminPanelSettings, "Admin"))
            }
            add(NavTabItem("perfil", Icons.Rounded.Person, Icons.Outlined.Person, "Perfil"))
        }
    }
    val indiceActivo = tabs.indexOfFirst { it.ruta == rutaActual }.coerceAtLeast(0)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = NegroObsidiana,
        border = BorderStroke(1.dp, BordeOscuro),
        shadowElevation = 10.dp
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 10.dp, end = 10.dp, top = 14.dp, bottom = 12.dp)
                .height(64.dp)
        ) {
            val anchoTab = maxWidth / tabs.size
            val margenPillHorizontal = if (tabs.size >= 4) 10.dp else 18.dp
            val desplazamientoPill by animateDpAsState(
                targetValue = anchoTab * indiceActivo,
                animationSpec = spring(
                    dampingRatio = 0.70f,
                    stiffness = Spring.StiffnessMediumLow
                ),
                label = "DesplazamientoNavbar"
            )

            Box(
                modifier = Modifier
                    .offset(x = desplazamientoPill)
                    .width(anchoTab)
                    .height(36.dp)
                    .padding(horizontal = margenPillHorizontal)
                    .clip(RoundedCornerShape(50))
                    .background(DoradoChampan)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { tab ->
                    val seleccionado = tab.ruta == rutaActual
                    val escala by animateFloatAsState(
                        targetValue = if (seleccionado) 1.12f else 0.96f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "EscalaIcono${tab.ruta}"
                    )
                    val colorIcono by animateColorAsState(
                        targetValue = if (seleccionado) NegroObsidiana else TextoSecundarioOscuro,
                        animationSpec = tween(durationMillis = 200),
                        label = "ColorIcono${tab.ruta}"
                    )
                    val colorTexto by animateColorAsState(
                        targetValue = if (seleccionado) DoradoChampan else TextoSecundarioOscuro,
                        animationSpec = tween(durationMillis = 200),
                        label = "ColorTexto${tab.ruta}"
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onNavegar(tab.ruta) }
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Box(
                            modifier = Modifier.height(36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (seleccionado) tab.iconoActivo else tab.iconoInactivo,
                                contentDescription = tab.etiqueta,
                                tint = colorIcono,
                                modifier = Modifier
                                    .size(22.dp)
                                    .scale(escala)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = tab.etiqueta,
                            color = colorTexto,
                            fontSize = 12.sp,
                            lineHeight = 15.sp,
                            fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
