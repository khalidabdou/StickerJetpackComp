package com.example.stickerjetpackcomp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.ui.theme.backgroundWhite
import com.example.stickerjetpackcomp.ui.theme.darkGray
import com.example.stickerjetpackcomp.viewModel.StickerViewModel
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.green.china.sticker.features.sticker.models.StickerPack
import com.skydoves.landscapist.glide.GlideImage
import kotlin.Int
import kotlin.Unit
import kotlin.repeat


@ExperimentalAnimationApi
@Composable
fun Home(navController: NavController, viewModel: StickerViewModel) {

    var visible by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    var scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(scaffoldState) {
        if (viewModel.stickers.value.isNullOrEmpty())
            viewModel.getStickers()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(icon = R.drawable.ic_baseline_star_half_24, onClick = {

            })
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            if (viewModel.stickers.value != null)
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(viewModel.stickers.value!!.size) {
                        Pack(viewModel.stickers.value!![it]) {
                            viewModel.setDetailPack(it)
                            navController.navigate(Screen.Details.route)
                        }
                    }
                }

        }
    }
}


@Composable
fun AppBar(icon: Int, background: Color = Color.White, onClick: () -> Unit) {
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
            painter = painterResource(id = icon),
            contentDescription = "",
            tint = backgroundWhite,
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    onClick()
                }
        )
        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_language_24),
            contentDescription = "",
            tint = backgroundWhite,
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    onClick()
                }
        )
        Spacer(modifier = Modifier.width(5.dp))
    }
}

@ExperimentalAnimationApi
@Composable
fun Pack(sticker: StickerPack, onClick: (StickerPack) -> Unit) {

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
                onClick(sticker)
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
                    .background(Color.White)
                    .border(BorderStroke(width = 1.dp, color = darkGray))
                    .padding(10.dp)
            ) {
                GlideImage(
                    imageModel = "${sticker.tray_image_file}",
                    // Crop, Fit, Inside, FillHeight, FillWidth, None
                    contentScale = ContentScale.Crop,
                    // shows a placeholder while loading the image.
                    placeHolder = ImageBitmap.imageResource(R.drawable.sticker),
                    // shows an error ImageBitmap when the request failed.
                    error = ImageBitmap.imageResource(R.drawable.sticker),
                    modifier = Modifier.size(70.dp)
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = sticker.name, color = darkGray, style = MaterialTheme.typography.h1)
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

                    AsyncImage(
                        model = "${sticker.stickers[it].image_file}",
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

