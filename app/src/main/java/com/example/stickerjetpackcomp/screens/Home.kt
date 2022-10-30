package com.example.stickerjetpackcomp.screens

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.model.Category
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.example.stickerjetpackcomp.ui.theme.colors
import com.example.stickerjetpackcomp.ui.theme.darkGray
import com.example.stickerjetpackcomp.utils.AppUtil
import com.example.stickerjetpackcomp.utils.Config
import com.example.stickerjetpackcomp.viewModel.StickerViewModel
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.google.android.gms.ads.AdSize
import com.ringtones.compose.feature.admob.AdvertView
import com.skydoves.landscapist.glide.GlideImage
import java.util.*


@OptIn(ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
@Composable
fun Home(navController: NavController, viewModel: StickerViewModel) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val packagename=context.packageName
    LaunchedEffect(scaffoldState) {
        if (viewModel.stickers.value.isNullOrEmpty())
            try {
                viewModel.getStickers(packagename)
            } catch (ex: Exception) {
                Toast.makeText(context, "Please try again", Toast.LENGTH_LONG).show()
            }


    }

    val state = rememberLazyListState()
    var editable by remember { mutableStateOf(false) }


    Scaffold(
        backgroundColor = Color.White,
        modifier = Modifier,
        scaffoldState = scaffoldState,
        topBar = {
            var message = "Good Morning"
            val c: Calendar = Calendar.getInstance()
            val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)

            if (timeOfDay >= 0 && timeOfDay < 12) {
                message = "Good Morning"
            } else if (timeOfDay >= 12 && timeOfDay < 16) {
                message = "Good Afternoon"
            } else if (timeOfDay >= 16 && timeOfDay < 21) {
                message = "Good Evening"
            } else if (timeOfDay >= 21 && timeOfDay < 24) {
                message = "Good Night"
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(darkGray)
                    .padding(5.dp)
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.h4,
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(5f)
                )
                AppBarIcon(icon = R.drawable.share) {
                    AppUtil.share(context)
                }
                AppBarIcon(icon = R.drawable.star) {
                    AppUtil.openStore(context)
                }
                Spacer(modifier = Modifier.width(3.dp))
                /*AppBarIcon(icon = R.drawable.share) {
                    shareWebp.share(context)
                }*/
            }
        },
        bottomBar = {
            AdvertView()
        }

    ) {
        LazyColumn(
            state = state,
            modifier = Modifier
                .padding(10.dp)
                .background(
                    Color.White
                )
        ) {
            item {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.h4,
                    color = darkGray,
                    modifier = Modifier.padding(start = 8.dp)
                )
                val listState = rememberLazyListState()
                LazyRow(
                    state = listState,
                    contentPadding = PaddingValues(8.dp),
                ) {
                    if (!viewModel.categories.value.isNullOrEmpty())
                        items(viewModel.categories.value!!.size) {
                            CategoryCompose(
                                color = colors[it],
                                cat = viewModel.categories.value!![it],
                                packagename = packagename,
                                onClick = {
                                    viewModel.cid = it
                                    navController.navigate(Screen.PacksByCategory.route)
                                })
                        } else {
                        item {
                            repeat(4) {
                                LoadingShimmerEffect()
                            }
                        }
//                        items(viewModel.categories.value!!.size) {
//                            CategoryCompose(
//                                colors[it],
//                                viewModel.categories.value!![it],
//                                packagename = packagename,
//                                onClick = {
//                                    viewModel.cid = it
//                                    navController.navigate(Screen.PacksByCategory.route)
//                                })
//                        }
                    }
                }

            }

            item {
                Text(
                    text = "Latest",
                    style = MaterialTheme.typography.h4,
                    color = darkGray,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            if (viewModel.stickers.value != null)
                items(viewModel.stickers.value!!.size) {
                    Popular(viewModel.stickers.value!![it],
                        onClick = {
                            viewModel.isReady.value = false
                            viewModel.index = 0
                            viewModel.setDetailPack(viewModel.stickers.value!![it])
                            navController.navigate(Screen.Details.route)
                        })
                }
            else item {
                repeat(6) {
                    LoadingShimmerEffect()
                }
            }
            item {
                Spacer(modifier = Modifier.height(AdSize.BANNER.height.dp))
            }

        }
    }
}

@Composable
fun AppBarIcon(icon: Int, onClick: () -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .size(36.dp)
        .clip(RoundedCornerShape(4.dp))
        .padding(6.dp)
        .clickable {
            onClick()
        }) {
        Icon(
            modifier = Modifier
                .size(30.dp),
            painter = painterResource(id = icon),
            tint = Color.White,
            contentDescription = "Logo Icon",
        )
    }
}

@Composable
fun CategoryCompose(
    color: Color,
    cat: Category,
    packagename: String,
    onClick: (Int) -> Unit
) {
    Box {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(0.4f))
                    .clickable {
                        onClick(cat.id)
                    },
                contentAlignment = Alignment.Center
            ) {
                GlideImage(
                    imageModel = Config.BASE_URL + "apps/${packagename}/category/${cat.image}",
                    // Crop, Fit, Inside, FillHeight, FillWidth, None
                    contentScale = ContentScale.Crop,
                    // shows a placeholder while loading the image.
                    placeHolder = ImageBitmap.imageResource(R.drawable.sticker),
                    // shows an error ImageBitmap when the request failed.
                    error = ImageBitmap.imageResource(R.drawable.sticker),
                    modifier = Modifier
                        .size(70.dp)
                        .padding(6.dp)
                )
            }
            Text(text = cat.name, style = MaterialTheme.typography.body1, color = Color.Black)


        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(14.dp)
                .width(30.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(color.copy(0.9f))

        ) {
            Text(
                text = cat.pack_stickers.size.toString(),
                style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold)
            )
        }
    }

}


@ExperimentalAnimationApi
@Composable
fun Popular(pack: StickerPack, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
    ) {
        Pack(sticker = pack, onClick = {
            onClick()
        })
    }
}


@Composable
fun ShimmerGridItem(brush: Brush) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp), verticalAlignment = Alignment.Top
    ) {
        Spacer(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(brush)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(fraction = 0.5f)
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(10.dp)) //creates an empty space between
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(fraction = 0.7f)
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(10.dp)) //creates an empty space between
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(fraction = 0.9f)
                    .background(brush)
            )
        }
    }
}


@Composable
fun LoadingShimmerEffect() {
    //These colors will be used on the brush. The lightest color should be in the middle
    val gradient = listOf(
        Color.LightGray.copy(alpha = 0.9f), //darker grey (90% opacity)
        Color.LightGray.copy(alpha = 0.3f), //lighter grey (30% opacity)
        Color.LightGray.copy(alpha = 0.9f)
    )

    val transition = rememberInfiniteTransition() // animate infinite times

    val translateAnimation = transition.animateFloat( //animate the transition
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000, // duration for the animation
                easing = FastOutLinearInEasing
            )
        )
    )
    val brush = linearGradient(
        colors = gradient,
        start = Offset(200f, 200f),
        end = Offset(
            x = translateAnimation.value,
            y = translateAnimation.value
        )
    )
    ShimmerGridItem(brush = brush)
}






