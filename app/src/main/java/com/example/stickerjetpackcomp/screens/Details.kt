package com.example.stickerjetpackcomp.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.ui.theme.backgroundWhite
import com.example.stickerjetpackcomp.ui.theme.darkGray
import com.example.stickerjetpackcomp.ui.theme.darkGray2

@Composable
fun Details() {

    Scaffold(topBar = { AppBar(background = darkGray2) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundWhite)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                    .background(darkGray2)
                    .padding(10.dp)
            ) {
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(
                            CircleShape
                        )
                        .background(darkGray)
                        .padding(10.dp)
                ) {
                    CustomComponent(
                        bigTextColor = backgroundWhite,
                        foregroundIndicatorColor = Green,
                        backgroundIndicatorColor = White,
                        backgroundIndicatorStrokeWidth = 15f,
                        foregroundIndicatorStrokeWidth = 15f
                    )

                }
                Spacer(modifier = Modifier.width(5.dp))
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Text(
                        text = "Sticker pack ",
                        color = backgroundWhite,
                        style = MaterialTheme.typography.h1
                    )
                    LabelLikes(icon = R.drawable.eye, text = "10k")
                    LabelLikes(icon = R.drawable.favorite, text = "1506")
                }
                Column() {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Green.copy(0.8f),
                            contentColor = darkGray
                        )
                    ) {
                        Text(text = "Add")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.whatsapp),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                }

            }

            GridStickers()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GridStickers() {
    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(Stickers.size) { index ->
            Card(
                modifier = Modifier
                    .size(90.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(10.dp)),
                backgroundColor = Color.LightGray,
            ) {
                Image(
                    painter = painterResource(id = Stickers[index]),
                    contentDescription = "",
                    modifier = Modifier.size(70.dp)
                )
            }
        }
    }
}

@Composable
fun LabelLikes(icon: Int, text: String) {
    Row(
        modifier = Modifier
            .height(20.dp)
    ) {

        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Logo Icon",
            tint = backgroundWhite,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, color = backgroundWhite.copy(0.9f))
    }
}

@Composable
fun CustomComponent(
    canvasSize: Dp = 300.dp,
    indicatorValue: Int = 50,
    maxIndicatorValue: Int = 100,
    backgroundIndicatorColor: Color = backgroundWhite,//MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
    backgroundIndicatorStrokeWidth: Float = 100f,
    foregroundIndicatorColor: Color = darkGray,
    foregroundIndicatorStrokeWidth: Float = 100f,
//    indicatorStrokeCap: StrokeCap = StrokeCap.Round,
    bigTextFontSize: TextUnit = MaterialTheme.typography.body1.fontSize,
    bigTextColor: Color = darkGray,
    bigTextSuffix: String = "%",
    smallTextFontSize: TextUnit = MaterialTheme.typography.body1.fontSize,
    smallTextColor: Color = darkGray.copy(alpha = 0.3f)
) {
    var allowedIndicatorValue by remember {
        mutableStateOf(maxIndicatorValue)
    }
    allowedIndicatorValue = if (indicatorValue <= maxIndicatorValue) {
        indicatorValue
    } else {
        maxIndicatorValue
    }

    var animatedIndicatorValue by remember { mutableStateOf(0f) }
    LaunchedEffect(key1 = allowedIndicatorValue) {
        animatedIndicatorValue = allowedIndicatorValue.toFloat()
    }

    val percentage =
        (animatedIndicatorValue / maxIndicatorValue) * 100

    val sweepAngle by animateFloatAsState(
        targetValue = (2.4 * percentage).toFloat(),
        animationSpec = tween(1000)
    )

    val receivedValue by animateIntAsState(
        targetValue = allowedIndicatorValue,
        animationSpec = tween(1000)
    )

    val animatedBigTextColor by animateColorAsState(
        targetValue = if (allowedIndicatorValue == 0)
            MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
        else
            bigTextColor,
        animationSpec = tween(1000)
    )

    Column(
        modifier = Modifier
            .size(canvasSize)
            .drawBehind {
                val componentSize = size / 1.25f
                backgroundIndicator(
                    componentSize = componentSize,
                    indicatorColor = backgroundIndicatorColor,
                    indicatorStrokeWidth = backgroundIndicatorStrokeWidth,
//                    indicatorStokeCap = indicatorStrokeCap
                )
                foregroundIndicator(
                    sweepAngle = sweepAngle,
                    componentSize = componentSize,
                    indicatorColor = foregroundIndicatorColor,
                    indicatorStrokeWidth = foregroundIndicatorStrokeWidth,
//                    indicatorStokeCap = indicatorStrokeCap
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmbeddedElements(
            bigText = receivedValue,
            bigTextFontSize = bigTextFontSize,
            bigTextColor = animatedBigTextColor,
            bigTextSuffix = bigTextSuffix,

            smallTextColor = smallTextColor,
            smallTextFontSize = smallTextFontSize
        )
    }
}

fun DrawScope.backgroundIndicator(
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
//    indicatorStokeCap: StrokeCap
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
}

fun DrawScope.foregroundIndicator(
    sweepAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
//    indicatorStokeCap: StrokeCap
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 90f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
}

@Composable
fun EmbeddedElements(
    bigText: Int,
    bigTextFontSize: TextUnit,
    bigTextColor: Color,
    bigTextSuffix: String,
    smallTextColor: Color,
    smallTextFontSize: TextUnit
) {
    Text(
        text = "$bigText ${bigTextSuffix.take(2)}",
        color = bigTextColor,
        fontSize = bigTextFontSize,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
}