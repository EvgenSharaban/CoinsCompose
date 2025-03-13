package com.example.coinscomp.presentation.uiviews

import android.util.TypedValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import com.example.coinscomp.core.widgets.RhombusView
import com.example.coinscomp.ui.theme.CoinsCompTheme

// used RhombusView from core/widgets, implemented ability to rounding corners and stretching
@Composable
fun RhombusTextView(
    text: String,
    modifier: Modifier = Modifier,
    backColor: Color = Color.Red,
    cornerRadius: Float = 0f,
    textColor: Color = Color.DarkGray,
    textSize: TextUnit = 16.sp
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            RhombusView(context).apply {
                this.updateRhombus(text, textColor, textSize, backColor, cornerRadius)
            }
        },
        update = { view ->
            view.updateRhombus(text, textColor, textSize, backColor, cornerRadius)
        }
    )
}

// alternative variant with rounded corners, can't be stretched
@Composable
fun RhombusTextView2(
    text: String,
    modifier: Modifier = Modifier,
    backColor: Color = Color.Red,
    cornerRadius: Float = 0f,
    textColor: Color = Color.White,
    textSize: TextUnit = 16.sp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .drawWithCache {
                val roundedPolygon = RoundedPolygon(
                    numVertices = 4,
                    radius = size.minDimension / 2,
                    centerX = size.width / 2,
                    centerY = size.height / 2,
                    rounding = CornerRounding(cornerRadius)
                )
                val roundedPolygonPath = roundedPolygon.toPath().asComposePath()
                onDrawBehind {
                    drawPath(
                        roundedPolygonPath,
                        color = backColor,
                    )
                }
            }
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = textSize,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp),
            maxLines = 1
        )
    }
}

// alternative variant without rounded corners, can be stretch, may have a stroke instead of a fill with rounded corners (commented)
@Composable
fun RhombusTextView3(
    text: String,
    modifier: Modifier = Modifier,
    backColor: Color = Color.Red,
    cornerRadius: Float = 0f,
    textColor: Color = Color.White,
    textSize: TextUnit = 16.sp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .drawBehind {
                val path = Path().apply {
                    val centerX = size.width / 2
                    val centerY = size.height / 2

                    moveTo(centerX, 0f) // High point
                    lineTo(size.width, centerY) // Right point
                    lineTo(centerX, size.height) // Low point
                    lineTo(0f, centerY) // Left point

                    close()
                }

                drawPath(
                    path = path,
                    color = backColor,
                    style = Fill
//                    style = Stroke(width = 2f, pathEffect = PathEffect.cornerPathEffect(cornerRadius)),
                )
            }
//            .padding(16.dp) // Adding indents
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = textSize,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp),
            maxLines = 1
        )
    }
}

private fun RhombusView.updateRhombus(
    text: String,
    textColor: Color,
    textSize: TextUnit,
    backColor: Color,
    cornerRadius: Float
) {
    setText(text)
    setTextColor(textColor.toArgb())
    setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.value)
    this.backColor = backColor.toArgb()
    this.cornerRadius = cornerRadius
}

@Preview(showBackground = true)
@Composable
private fun RhombusTextPreview() {
    CoinsCompTheme {
        RhombusTextView(
            text = "12",
            modifier = Modifier
                .width(88.dp)
                .height(60.dp),
            backColor = Color.Blue,
            cornerRadius = 16f,
            textColor = Color.Red,
            textSize = 22.sp
        )
    }
}