package com.example.stickerjetpackcomp.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.model.AdProvider.Companion.Banner
import com.example.stickerjetpackcomp.model.Category
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.example.stickerjetpackcomp.utils.AppTheme.Companion.DETAILS
import com.example.stickerjetpackcomp.utils.AppTheme.Companion.SHAPE
import com.example.stickerjetpackcomp.utils.AppUtil
import com.example.stickerjetpackcomp.utils.Config
import com.example.stickerjetpackcomp.viewModel.StickerViewModel
import com.example.testfriends_jetpackcompose.navigation.Screen
import com.google.android.gms.ads.AdSize
import com.ringtones.compose.feature.admob.AdvertView
import com.skydoves.landscapist.glide.GlideImage
import kotlin.random.Random


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@Composable
fun Home(navController: NavController, viewModel: StickerViewModel) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val packagename = context.packageName
    val message = viewModel.message.collectAsState()
    LaunchedEffect(scaffoldState) {
        if (viewModel.stickers.value.isNullOrEmpty())
            try {
                viewModel.getStickers(context.packageName)
            } catch (ex: Exception) {
                Toast.makeText(context, "Please try again", Toast.LENGTH_LONG).show()
            }
    }


    val state = rememberLazyListState()


    var showAlertDialog by remember { mutableStateOf(false) }

    val activity = (context as? Activity)
    BackHandler {
        showAlertDialog = true
    }



    Scaffold(
        modifier = Modifier,
        topBar = {
            //var message = "Good Morning"
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(5.dp)
            ) {
                Text(
                    text = message.value,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
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
            if (Banner.ad_status)
                AdvertView()
        }

    ) {
        LazyColumn(
            state = state,
            modifier = Modifier
                .padding(it)
                .background(
                    MaterialTheme.colorScheme.background
                )
        ) {
            item {
//                Text(
//                    text = stringResource(R.string.Categories),
//                    style = MaterialTheme.typography.titleLarge,
//                    color = MaterialTheme.colorScheme.secondary,
//                    modifier = Modifier.padding(start = 8.dp)
//                )
                val listState = rememberLazyListState()
                LazyRow(
                    state = listState,
                    contentPadding = PaddingValues(8.dp),
                ) {
                    if (!viewModel.categories.value.isNullOrEmpty())
                        items(viewModel.categories.value!!.size) {
                            CategoryCompose(
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
                    }
                }
            }
            item {
                Text(
                    text = stringResource(R.string.packs),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
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
                            navController.navigate(DETAILS.route)
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

        if (showAlertDialog) {
            AlertDialog(
                onDismissRequest = {
                    showAlertDialog = false
                },
                title = {
                    Text(
                        stringResource(R.string.sure),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                text = {
                    val apps = viewModel.apps.value

                    if (!apps.isNullOrEmpty()) {
                        val app = apps.get(Random.nextInt(0, apps.size))
                        AdBannerApp(app)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            AppUtil.openStore(context)

                        }) {
                        Text(stringResource(R.string.rate))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            //showAlertDialog=false
                            activity?.finish()
                        }) {
                        Text(stringResource(R.string.quit))
                    }
                },


                )
        }
    }
}

@Composable
fun AppBarIcon(icon: Int, onClick: () -> Unit) {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
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
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = "Logo Icon",
        )
    }
}

@Composable
fun CategoryCompose(
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
                    .clip(SHAPE)
                    .background(MaterialTheme.colorScheme.primaryContainer)
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
                    placeHolder = ImageBitmap.imageResource(R.mipmap.ic_launcher_foreground),
                    // shows an error ImageBitmap when the request failed.
                    error = ImageBitmap.imageResource(R.mipmap.ic_launcher_foreground),
                    modifier = Modifier
                        .size(70.dp)
                        .padding(6.dp)
                )
            }
            Text(
                text = cat.name.take(10),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(30.dp)
                .clip(SHAPE)
                .background(MaterialTheme.colorScheme.onPrimaryContainer)

        ) {
            Text(
                text = cat.pack_stickers.size.toString(),
                style = TextStyle(
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    fontWeight = FontWeight.Bold
                )
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
                .clip(SHAPE)
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
                    .clip(SHAPE)
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






