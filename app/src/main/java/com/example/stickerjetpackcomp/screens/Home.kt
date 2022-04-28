package com.example.stickerjetpackcomp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.ui.theme.backgroundWhite
import com.example.stickerjetpackcomp.ui.theme.darkGray
import com.example.testfriends_jetpackcompose.navigation.Screen
import kotlinx.coroutines.flow.collectLatest


@ExperimentalAnimationApi
@Composable
fun Home(navController: NavController) {

    var visible by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    var scrollState = rememberScrollState()

    Scaffold(
        topBar = { AppBar() }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundWhite)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(10) {

                    Pack {
                       navController.navigate(Screen.Details.route)
                    }
                }

            }

        }
    }
}


@Composable
fun AppBar(background: Color = backgroundWhite) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(background)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
            .height(70.dp)
            .background(
                darkGray
            )
    ) {
        Text(
            text = "Love Stickers",
            color = backgroundWhite,
            modifier = Modifier.weight(4f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h1
        )
        Icon(
            painter = painterResource(id = R.drawable.favorite),
            contentDescription = "",
            tint = backgroundWhite,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
    }
}

@ExperimentalAnimationApi
@Composable
fun Pack(onClick: () -> Unit) {

    var visible by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(10.dp)
            .border(
                BorderStroke(width = 1.dp, color = darkGray.copy(0.5f)),
                RoundedCornerShape(10.dp)
            )
            .clickable {
                onClick()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Spacer(modifier = Modifier.width(5.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        CircleShape
                    )
                    .background(darkGray)
                    .padding(5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sticker_4),
                    contentDescription = "",

                    )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Sticker pack ", color = darkGray, style = MaterialTheme.typography.h1)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            LaunchedEffect(true) {
                visible = true
            }
            repeat(4) {
                AnimatedVisibility(
                    visible = visible,
                    enter = expandHorizontally(
                        // Expand from the top.
                        expandFrom = Alignment.Start,
                        animationSpec = tween(
                            durationMillis = 1000
                        )
                    ) + fadeIn(
                        initialAlpha = 0.3f
                    ),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut(),
                ) {

                Image(
                        painter = painterResource(id = Stickers[it]),
                        contentDescription = "",
                        modifier = Modifier.size(70.dp)
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.Add, contentDescription = "", modifier = Modifier
                    .size(30.dp)
                    .weight(1f), tint = darkGray
            )
        }
    }


}


val Stickers =
    listOf(R.drawable.sticker_2, R.drawable.sticker_3, R.drawable.sticker_4, R.drawable.sticker_5)

