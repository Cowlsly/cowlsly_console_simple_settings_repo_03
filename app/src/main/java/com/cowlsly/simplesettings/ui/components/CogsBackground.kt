package com.cowlsly.simplesettings.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.cowlsly.simplesettings.ui.theme.CowlslyBgDeep
import com.cowlsly.simplesettings.ui.theme.CowlslyBgMid
import com.cowlsly.simplesettings.ui.theme.CowlslyBgTop
import com.cowlsly.simplesettings.ui.theme.CowlslyCyan
import com.cowlsly.simplesettings.ui.theme.CowlslyGoldMid

@Composable
fun CogsBackground(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "cogs")
    val mainRotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(48_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "mainCog",
    )
    val smallRotation by transition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(24_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "smallCog",
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(CowlslyBgTop, CowlslyBgMid, CowlslyBgDeep),
                ),
            ),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridColor = CowlslyCyan.copy(alpha = 0.12f)
            val step = size.minDimension / 8f
            var x = 0f
            while (x < size.width) {
                drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f)
                x += step
            }
            var y = 0f
            while (y < size.height) {
                drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
                y += step
            }

            val center = Offset(size.width / 2f, size.height / 2f)
            rotate(mainRotation, center) {
                drawGear(center, radius = size.minDimension * 0.22f, teeth = 8)
            }
            rotate(smallRotation, Offset(size.width * 0.22f, size.height * 0.3f)) {
                drawGear(Offset(size.width * 0.22f, size.height * 0.3f), radius = size.minDimension * 0.1f, teeth = 6)
            }
            rotate(mainRotation * 1.3f, Offset(size.width * 0.78f, size.height * 0.68f)) {
                drawGear(Offset(size.width * 0.78f, size.height * 0.68f), radius = size.minDimension * 0.14f, teeth = 6)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.48f)),
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawGear(
    center: Offset,
    radius: Float,
    teeth: Int,
) {
    drawCircle(
        color = CowlslyGoldMid.copy(alpha = 0.35f),
        radius = radius,
        center = center,
        style = Stroke(width = radius * 0.08f),
    )
    drawCircle(
        color = CowlslyCyan.copy(alpha = 0.25f),
        radius = radius * 0.75f,
        center = center,
        style = Stroke(width = radius * 0.05f),
    )
    val toothLen = radius * 0.35f
    val inner = radius * 0.65f
    for (i in 0 until teeth) {
        val angle = (360f / teeth) * i
        val rad = Math.toRadians(angle.toDouble())
        val cos = kotlin.math.cos(rad).toFloat()
        val sin = kotlin.math.sin(rad).toFloat()
        drawLine(
            color = CowlslyGoldMid.copy(alpha = 0.4f),
            start = Offset(center.x + inner * cos, center.y + inner * sin),
            end = Offset(center.x + (inner + toothLen) * cos, center.y + (inner + toothLen) * sin),
            strokeWidth = radius * 0.06f,
        )
    }
    drawCircle(
        color = CowlslyCyan.copy(alpha = 0.5f),
        radius = radius * 0.18f,
        center = center,
        style = Stroke(width = radius * 0.04f),
    )
}