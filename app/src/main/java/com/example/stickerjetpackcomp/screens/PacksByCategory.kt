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
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.example.stickerjetpackcomp.ui.theme.backgroundWhite
import com.example.stickerjetpackcomp.ui.theme.colors
import com.example.stickerjetpackcomp.ui.theme.darkGray
import com.example.stickerjetpackcomp.viewModel.StickerViewModel
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalAnimationApi
@Composable
fun PacksByCategory(navController: NavController, viewModel: StickerViewModel) {
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
        backgroundColor = Color.White
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(Color.White)
        ) {
            if (viewModel.stickers.value != null)
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(viewModel.stickers.value!!.size) {
                        Column(  horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)) {
                            Pack(viewModel.stickers.value!![it]) {
                                viewModel.setDetailPack(it)
                                navController.navigate(Screen.Details.route)
                            }
                        }

                    }
                }

        }
    }
}


@ExperimentalAnimationApi
@Composable
fun Pack(sticker: StickerPack, onClick: (StickerPack) -> Unit) {

    var visible by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    Box(
      modifier = Modifier
          .clip(RoundedCornerShape(5.dp))
          .background(backgroundWhite.copy(0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(10.dp)
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
                Text(text = sticker.name, color = darkGray, style = MaterialTheme.typography.h5)
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



}
