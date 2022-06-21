package com.example.stickerjetpackcomp.screens

import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.example.stickerjetpackcomp.ui.theme.darkGray
import com.example.stickerjetpackcomp.viewModel.StickerViewModel
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.skydoves.landscapist.glide.GlideImage
import okhttp3.internal.filterList

@ExperimentalAnimationApi
@Composable
fun PacksByCategory(navController: NavController, viewModel: StickerViewModel) {
    var visible by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    var scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()
    val context= LocalContext.current
    LaunchedEffect(scaffoldState) {
        if (viewModel.stickers.value.isNullOrEmpty()){
            viewModel.getStickers(context)
        }
        viewModel.stickersByCat()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {}
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            if (!viewModel.stickerByCat.value.isNullOrEmpty())
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(viewModel.stickerByCat.value!!.size) {
                        Pack(viewModel.stickerByCat.value!![it]) {
                            viewModel.setDetailPack(it)
                            navController.navigate(Screen.Details.route)
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
                    .padding(10.dp)
            ) {
                GlideImage(
                    imageModel = "${sticker.stickers[0].image_file}",
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
            if (sticker.animated_sticker_pack)
            Image(painter = painterResource(id = R.drawable.gif_24), contentDescription = "",
                modifier = Modifier.size(30.dp)
            )
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
