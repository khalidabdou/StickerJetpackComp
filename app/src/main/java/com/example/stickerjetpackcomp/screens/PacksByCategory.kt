package com.example.stickerjetpackcomp.screens

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.model.AdProvider
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.example.stickerjetpackcomp.utils.AppTheme.Companion.DETAILS
import com.example.stickerjetpackcomp.viewModel.StickerViewModel
import com.ringtones.compose.feature.admob.AdvertViewAdmob
import com.skydoves.landscapist.glide.GlideImage
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@Composable
fun PacksByCategory(navController: NavController, viewModel: StickerViewModel) {

    var context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(scaffoldState) {
        if (viewModel.stickers.value.isNullOrEmpty()) {
            viewModel.getStickers(context.packageName)
        }
        viewModel.stickersByCat()
    }

    Scaffold(
        modifier = Modifier.background(
            MaterialTheme.colorScheme.background
        ),
        topBar = {},
        bottomBar = {
            AdvertViewAdmob()
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
        ) {
            if (!viewModel.stickerByCat.value.isNullOrEmpty())
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (!viewModel.apps.value.isNullOrEmpty()) {
                        val app = Random.nextInt(0, viewModel.apps.value!!.size)
                        item {
                            if (viewModel.apps.value != null)
                                Box(
                                    modifier = Modifier
                                        .padding(it)
                                        .border(
                                            2.dp, MaterialTheme.colorScheme.primary,
                                            RoundedCornerShape(6.dp)
                                        )
                                ) {
                                    AdBannerApp(viewModel.apps.value!![app])
                                }

                        }
                    }
                    items(viewModel.stickerByCat.value!!.size) {
                        Pack(viewModel.stickerByCat.value!![it]) {
                            viewModel.setDetailPack(it)
                            navController.navigate(DETAILS.route)
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(70.dp))
                    }
                }
        }
    }
}


@ExperimentalAnimationApi
@Composable
fun Pack(sticker: StickerPack, onClick: (StickerPack) -> Unit) {
    var visible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(10.dp)
            .border(
                BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primaryContainer),
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
                    .size(60.dp)
                    .clip(
                        CircleShape
                    )
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(10.dp)
            ) {
                GlideImage(
                    imageModel = "${sticker.tray_image_file}",
                    contentScale = ContentScale.Crop,
                    placeHolder = ImageBitmap.imageResource(R.mipmap.ic_launcher_foreground),
                    error = ImageBitmap.imageResource(R.mipmap.ic_launcher_foreground),
                    modifier = Modifier.size(70.dp)

                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = sticker.name,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
            if (sticker.animated_sticker_pack)
                Icon(
                    painter = painterResource(id = R.drawable.gif_24),
                    tint = Color.Red,
                    contentDescription = "",
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
                    Log.d("sto", "${sticker.stickers[it].image_file}")
                }
            }

        }
    }


}
