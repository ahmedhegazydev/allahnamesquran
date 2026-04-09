package app.asmaquran.mobile.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun GoogleProviderMark(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val stroke = size.minDimension * 0.18f
        val inset = stroke / 2
        val arcSize = Size(size.width - stroke, size.height - stroke)
        val topLeft = Offset(inset, inset)

        drawArc(
            color = Color(0xFFEA4335),
            startAngle = 205f,
            sweepAngle = 78f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = 128f,
            sweepAngle = 77f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 52f,
            sweepAngle = 76f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -28f,
            sweepAngle = 92f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawLine(
            color = Color(0xFF4285F4),
            start = Offset(size.width * 0.54f, size.height * 0.51f),
            end = Offset(size.width * 0.92f, size.height * 0.51f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun GithubProviderMark(
    modifier: Modifier = Modifier,
    iconTint: Color = Color(0xFF111827)
) {
    Canvas(modifier = modifier.size(22.dp)) {
        val stroke = size.minDimension * 0.11f
        val outline = Path().apply {
            moveTo(size.width * 0.30f, size.height * 0.42f)
            quadraticTo(size.width * 0.24f, size.height * 0.28f, size.width * 0.34f, size.height * 0.15f)
            lineTo(size.width * 0.45f, size.height * 0.24f)
            quadraticTo(size.width * 0.50f, size.height * 0.14f, size.width * 0.55f, size.height * 0.24f)
            lineTo(size.width * 0.66f, size.height * 0.15f)
            quadraticTo(size.width * 0.76f, size.height * 0.28f, size.width * 0.70f, size.height * 0.42f)
            quadraticTo(size.width * 0.69f, size.height * 0.57f, size.width * 0.58f, size.height * 0.66f)
            lineTo(size.width * 0.42f, size.height * 0.66f)
            quadraticTo(size.width * 0.31f, size.height * 0.57f, size.width * 0.30f, size.height * 0.42f)
        }

        drawPath(
            path = outline,
            color = iconTint,
            style = Stroke(
                width = stroke,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        drawLine(
            color = iconTint,
            start = Offset(size.width * 0.44f, size.height * 0.66f),
            end = Offset(size.width * 0.44f, size.height * 0.86f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )

        drawLine(
            color = iconTint,
            start = Offset(size.width * 0.56f, size.height * 0.66f),
            end = Offset(size.width * 0.56f, size.height * 0.86f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}
